package com.example.patientappointments.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name="appointments")
public class Appointment {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private @Column(name="PATIENT_ID") Long patientId;
    private @Column(name="DATE") LocalDate appointmentDate;
    private @Column(name="TIME") LocalTime appointmentTime;
    private @Transient String patientName;

    // Constructors
    public Appointment(){}
    public Appointment(Long patientId, LocalDate appointmentDate, LocalTime appointmentTime) {
        this.patientId = patientId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }

    // Getters
    public Long getId() {
        return id;
    }
    public Long getPatientID() {
        return patientId;
    }
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }
    public LocalTime getAppointmentTime() { return appointmentTime; }
    public  String getPatientName() {
        return patientName;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    @Override
    public String toString() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        String formattedTime = appointmentTime.format(timeFormatter);
        return "Appointment{" + "id=" + this.id + ", patientId='" + this.patientId + '\''
                + ", appointmentDate='" + this.appointmentDate + '\''
                + ", appointmentTime='" + formattedTime+ '\'' + '}';

    }
}
