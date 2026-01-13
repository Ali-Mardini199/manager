package com.appointments.manager.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.appointments.manager.dto.AppoitnmentStatus;
import com.appointments.manager.model.Appointment;
import com.appointments.manager.model.Service;
import com.appointments.manager.model.User;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
                SELECT a
                FROM Appointment a
                WHERE a.service.id = :serviceId
                AND a.status in :statusList
                AND (
                    (
                    a.startDate <= :startDate
                    AND a.endDate >= :startDate
                    )
                    OR
                    (
                    a.startDate <= :endDate
                    AND a.endDate >= :endDate
                    )
                )
            """)
    List<Appointment> findOverlappingAppointments(
            @Param("serviceId") Long serviceId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("statusList") List<AppoitnmentStatus> appoitnmentStatus);

    List<Appointment> findByUser(User user);

    List<Appointment> findByServiceIn(List<Service> services);
}
