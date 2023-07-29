package com.example.patientappointments.repository;

import com.example.patientappointments.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT p.patientName FROM Patient p WHERE p.id = :id")
    String findPatientNameById(@Param("id") Long id);

    @Query("SELECT a FROM Appointment a JOIN Patient p ON a.patientId = p.id WHERE LOWER(p.patientName) = LOWER(:patientName)")
    List<Appointment> findAppointmentsByPatientNameIgnoreCase(@Param("patientName") String patientName);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate = :appointmentDate")
    List<Appointment> findAppointmentsByDate(@Param("appointmentDate") LocalDate appointmentDate);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate = :appointmentDate AND a.appointmentTime = :appointmentTime")
    Appointment findAppointmentByDateAndTime(@Param("appointmentDate") LocalDate appointmentDate,
                                             @Param("appointmentTime") LocalTime appointmentTime);

}
