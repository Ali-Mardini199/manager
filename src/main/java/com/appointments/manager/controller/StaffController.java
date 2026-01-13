package com.appointments.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appointments.manager.dto.AppoitnmentStatus;
import com.appointments.manager.model.Appointment;
import com.appointments.manager.service.AppointmentService;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/appointment")
    public List<Appointment> getAll() {
        return appointmentService.getStaffAppointments();
    }
}
