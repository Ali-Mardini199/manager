package com.appointments.manager.dto;

import java.time.LocalDateTime;

public class CreateAppointmentDto {
    private Long serviceId;
    private LocalDateTime date; // start date

    public CreateAppointmentDto() {
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
