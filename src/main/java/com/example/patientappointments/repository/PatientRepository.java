package com.example.patientappointments.repository;

import com.example.patientappointments.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByName(String name);

    List<Patient> findByDateOfBirth(LocalDate dateOfBirth);

    List<Patient> findByNameAndDateOfBirth(String name, LocalDate dateOfBirth);

    Optional<Patient> findPatientByNameAndDateOfBirth(String name, LocalDate datOfBirth);


}
