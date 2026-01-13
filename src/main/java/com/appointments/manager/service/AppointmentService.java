package com.appointments.manager.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.appointments.manager.annotation.TrackExecution;
import com.appointments.manager.dto.NotificationMessage;
import com.appointments.manager.exception.BusinessException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.appointments.manager.dto.AppoitnmentStatus;
import com.appointments.manager.dto.CreateAppointmentDto;
import com.appointments.manager.model.Appointment;
import com.appointments.manager.model.Service;
import com.appointments.manager.model.User;
import com.appointments.manager.repository.AppointmentRepository;
import com.appointments.manager.repository.ServiceRepository;
import com.appointments.manager.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@org.springframework.stereotype.Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private NotificationService notificationService;

    @TrackExecution
    @Transactional
    public Appointment create(CreateAppointmentDto createAppointmentDto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("user not found");
        }
        User user = userOptional.get();
        Optional<Service> serviceOptional = serviceRepository.findById(createAppointmentDto.getServiceId());
        if (serviceOptional.isEmpty()) {
            throw new EntityNotFoundException("service not found");
        }
        Service service = serviceOptional.get();
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), service.getStartDate());
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), service.getEndDate());
        if (createAppointmentDto.getDate().plusMinutes(service.getPeriod()).isAfter(endDateTime)
                || createAppointmentDto.getDate().isBefore(startDateTime)) {
            throw new BusinessException("service is not available at this time");
        }
        List<Appointment> overlappingAppointments = appointmentRepository.findOverlappingAppointments(
                createAppointmentDto.getServiceId(),
                createAppointmentDto.getDate(),
                createAppointmentDto.getDate().plusMinutes(service.getPeriod()),
                List.of(AppoitnmentStatus.APPROVED, AppoitnmentStatus.PENDING));
        if (!overlappingAppointments.isEmpty()) {
            throw new BusinessException("there is already an appointment in the same date");
        }
        int appointmentCount = 0;
        if (user.getAppointmentCount() != null) {
            appointmentCount = user.getAppointmentCount();
        }
        user.setAppointmentCount(appointmentCount + 1);
        Appointment appointment = new Appointment();
        appointment.setService(service);
        appointment.setEndDate(createAppointmentDto.getDate().plusMinutes(service.getPeriod()));
        appointment.setStartDate(createAppointmentDto.getDate());
        appointment.setStatus(AppoitnmentStatus.PENDING);
        appointment.setUser(user);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public Appointment findById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    public Appointment update(Long id, Appointment updated) {
        Appointment appointment = findById(id);
        appointment.setUser(updated.getUser());
        appointment.setService(updated.getService());
        appointment.setStartDate(updated.getStartDate());
        appointment.setEndDate(updated.getEndDate());
        appointment.setStatus(updated.getStatus());
        return appointmentRepository.save(appointment);
    }

    public void delete(Long id) {
        appointmentRepository.deleteById(id);
    }

    public Appointment updateStatus(Long id, AppoitnmentStatus status) throws Exception {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);
        if (appointmentOptional.isEmpty()) {
            throw new Exception("appointment not found");
        }
        Appointment appointment = appointmentOptional.get();
        appointment.setStatus(status);
        appointmentRepository.save(appointment);
        notificationService.notifyUser(
                appointment.getUser().getUsername(),
                new NotificationMessage(
                        "appointment approved",
                        "Your appointment was approved successfully",
                        Instant.now()));
        return appointment;
    }

    public Appointment cancelAppointment(Long id) throws Exception {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);
        if (appointmentOptional.isEmpty()) {
            throw new EntityNotFoundException("appointment not found");
        }
        Appointment appointment = appointmentOptional.get();
        appointment.setStatus(AppoitnmentStatus.CANCELED);
        appointmentRepository.save(appointment);
        return appointment;
    }

    public List<Appointment> getStaffAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("user not found");
        }
        User user = userOptional.get();
        List<Service> services = serviceRepository.findByProvider(user);
        List<Appointment> appointments = appointmentRepository.findByServiceIn(services);
        return appointments;
    }

    public List<Appointment> getMyAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("user not found");
        }
        User user = userOptional.get();
        return appointmentRepository.findByUser(user);
    }
}