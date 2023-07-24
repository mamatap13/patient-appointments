package com.example.patientappointments.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="appointments")
public class Appointment {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PATIENT_ID")
    private Patient patient;
    @Column(name="APPOINTMENT_DATETIME")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentDateTime;

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

}
