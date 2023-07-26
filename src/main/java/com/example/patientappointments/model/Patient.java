package com.example.patientappointments.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Entity
@Table(name="patients")
public class Patient {
    private @Id @GeneratedValue Long id;

    private @Column(name="PATIENT_NAME") String name;
    private @Column(name="PATIENT_DOB") LocalDate dateOfBirth;
    public Patient() {}
    public Patient(String name, LocalDate dateOfBirth) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "Patient{" + "id=" + this.id + ", name='" + this.name + '\'' + ", dateOfBirth='" + this.dateOfBirth + '\'' + '}';
    }


}
