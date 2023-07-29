package com.example.patientappointments.services;

import com.example.patientappointments.exceptions.PatientException;
import com.example.patientappointments.model.Patient;
import com.example.patientappointments.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Optional<Patient> getPatientByName(String patientName) {
        return patientRepository.findByPatientName(patientName);
    }

    public Long getPatientIdByNameAndDateOfBirth(String patientName, LocalDate dateOfBirth) {
        Optional<Patient> patient = patientRepository.findPatientByPatientNameAndDateOfBirth(patientName, dateOfBirth);
        if(patient.isPresent()) {
            return patient.get().getId();
        }
        throw new PatientException("Patient does not exists");
    }

    public List<Patient> getPatientByDateOfBirth(LocalDate dateOfBirth) {
        return patientRepository.findByDateOfBirth(dateOfBirth);
    }

    public List<Patient> getPatientByNameAndDateOfBirth(String patientName, LocalDate dateOfBirth) {
        return patientRepository.findByPatientNameAndDateOfBirth(patientName, dateOfBirth);
    }

    public Patient createPatient(Patient patient) {
        Optional<Patient> existingPatient = patientRepository.findPatientByPatientNameAndDateOfBirth(
                patient.getPatientName(), patient.getDateOfBirth());
        if(existingPatient.isPresent()) {
            throw new PatientException("Patient with the given name and date of birth already exists");
        }
        return patientRepository.save(patient);
    }

    public Map<Patient, String> createMultiplePatients(List<Patient> patients) {
        Map<Patient, String> newPatients = new LinkedHashMap<>();

        for(Patient patient : patients) {
            Optional<Patient> existingPatient = patientRepository.findPatientByPatientNameAndDateOfBirth(
                    patient.getPatientName(), patient.getDateOfBirth());

            if(existingPatient.isPresent()) {
                newPatients.put(patient, "Creation failed, patient already exists");
            } else {
                patientRepository.save(patient);
                newPatients.put(patient, "Creation successful");
            }
        }

        return newPatients;
    }

    public void deletePatient(Long patientId) {
        patientRepository.deleteById(patientId);
    }


}
