package com.example.patientappointments.repository;

import com.example.patientappointments.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {


    Optional<Patient> findByPatientName(String patientName);

    List<Patient> findByDateOfBirth(LocalDate dateOfBirth);

    List<Patient> findByPatientNameAndDateOfBirth(String patientName, LocalDate dateOfBirth);

    Optional<Patient> findPatientByPatientNameAndDateOfBirth(String patientName, LocalDate datOfBirth);


}
