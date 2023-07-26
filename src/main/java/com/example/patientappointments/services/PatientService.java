package com.example.patientappointments.services;

import com.example.patientappointments.exceptions.PatientException;
import com.example.patientappointments.model.Patient;
import com.example.patientappointments.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public List<Patient> getPatientByName(String name) {
        return  patientRepository.findByName(name);
    }

    public List<Patient> getPatientByDateOfBirth(LocalDate dateOfBirth) {
        return patientRepository.findByDateOfBirth(dateOfBirth);
    }

    public List<Patient> getPatientByNameAndDateOfBirth(String name, LocalDate dateOfBirth) {
        return patientRepository.findByNameAndDateOfBirth(name, dateOfBirth);
    }

    public Patient createPatient(Patient patient) {
        Optional<Patient> existingPatient = patientRepository.findPatientByNameAndDateOfBirth(
                patient.getName(), patient.getDateOfBirth());
        if(existingPatient.isPresent()) {
            throw new PatientException("Patient with the given name and date of birth already exists");
        }
        return patientRepository.save(patient);
    }

    public List<Patient> createMultiplePatients(List<Patient> patients) {
        List<Patient> newPatients = new ArrayList<>();
        int existingPatientCount = 0;
        for(Patient patient : patients) {
            Optional<Patient> existingPatient = patientRepository.findPatientByNameAndDateOfBirth(
                    patient.getName(), patient.getDateOfBirth());
            if(existingPatient.isPresent()) {
                // skip existing patient
                existingPatientCount++;
                continue;
            }

            newPatients.add(patient);
        }

        return patientRepository.saveAll(patients);
    }

    public void deletePatient(Patient patient) {
        patientRepository.delete(patient);
    }


}
