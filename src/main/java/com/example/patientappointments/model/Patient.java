package com.example.patientappointments.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Entity
@Table(name="patients")
public class Patient {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    private @Column(name="PATIENT_NAME") String patientName;
    private @Column(name="PATIENT_DOB") LocalDate dateOfBirth;
    public Patient() {}
    public Patient(String patientName, LocalDate dateOfBirth) {
        this.patientName = patientName;
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientName(){
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }


    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "Patient{" + "id=" + this.id + ", name='" + this.patientName + '\'' + ", dateOfBirth='" + this.dateOfBirth + '\'' + '}';
    }


}
