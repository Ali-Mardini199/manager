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
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AppointmentService appointmentService;

    @PutMapping("/appointment/{id}/status/{status}")
    public Appointment approve(@PathVariable Long id, @PathVariable AppoitnmentStatus status) throws Exception {
        return appointmentService.updateStatus(id, status);
    }

    @DeleteMapping("/appointment/{id}")
    public void delete(@PathVariable Long id) {
        appointmentService.delete(id);
    }

    @GetMapping("/appointment")
    public List<Appointment> getAll() {
        return appointmentService.findAll();
    }
}
