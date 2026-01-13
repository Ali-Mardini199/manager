package com.appointments.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appointments.manager.dto.CreateAppointmentDto;
import com.appointments.manager.model.Appointment;
import com.appointments.manager.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public Appointment create(@RequestBody CreateAppointmentDto createAppointmentDto) throws Exception {
        return appointmentService.create(createAppointmentDto);
    }

    @PutMapping("/{id}")
    public Appointment update(@PathVariable Long id, @RequestBody Appointment appointment) {
        return appointmentService.update(id, appointment);
    }

    @PutMapping("/cancel/{id}")
    public Appointment cancelAppointment(@PathVariable Long id) throws Exception {
        return appointmentService.cancelAppointment(id);
    }

    @GetMapping("/appointment")
    public List<Appointment> getMyAppointments() {
        return appointmentService.getMyAppointments();
    }
}
