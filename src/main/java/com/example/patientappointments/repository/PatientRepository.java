package com.example.patientappointments.repository;

import com.example.patientappointments.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByName(String name);
}
