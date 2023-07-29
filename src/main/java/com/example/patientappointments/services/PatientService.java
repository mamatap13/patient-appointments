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

    /**
     * Get all patients from repository
     * @return list of patients
     */
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * Get patient associated with given id from repo
     * @param id
     * @return patient
     */
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    /**
     * Get patient(s) associated with given name(ignore case) from repo
     * @param patientName
     * @return patient(s)
     */
    public Optional<Patient> getPatientByName(String patientName) {
        return patientRepository.findByPatientNameIgnoreCase(patientName);
    }

    /**
     * Get patient(s) associated with given date of birth
     * @param dateOfBirth
     * @return patient(s)
     */
    public List<Patient> getPatientByDateOfBirth(LocalDate dateOfBirth) {
        return patientRepository.findByDateOfBirth(dateOfBirth);
    }

    /**
     * Get patient id associated with given name and date of birth
     * If patient does not exist throw patient exception
     * @param patientName
     * @param dateOfBirth
     * @return id
     */
    public Long getPatientIdByNameAndDateOfBirth(String patientName, LocalDate dateOfBirth) {
        Optional<Patient> patient = patientRepository.findPatientByPatientNameIgnoreCaseAndDateOfBirth(patientName, dateOfBirth);
        if(patient.isPresent()) {
            return patient.get().getId();
        }
        throw new PatientException("Patient does not exists");
    }

    /**
     * Get patient associated with given name(ignore case) and date of birth
     * @param patientName
     * @param dateOfBirth
     * @return patient
     */
    public Patient getPatientByNameAndDateOfBirth(String patientName, LocalDate dateOfBirth) {
        return patientRepository.findByPatientNameIgnoreCaseAndDateOfBirth(patientName, dateOfBirth);
    }

    /**
     * Create a new patients
     * If patient with given name and date of birth already exists, throw exception
     * @param patient
     * @return new patient
     */
    public Patient createPatient(Patient patient) {
        Optional<Patient> existingPatient = patientRepository.findPatientByPatientNameIgnoreCaseAndDateOfBirth(
                patient.getPatientName(), patient.getDateOfBirth());
        if(existingPatient.isPresent()) {
            throw new PatientException("Patient with the given name and date of birth already exists");
        }
        return patientRepository.save(patient);
    }

    /**
     * Create multiple patients
     * Takes in a list of patients, adds success/fail message
     * @param patients
     * @return map with new patient objects with success/fail message
     */
    public Map<Patient, String> createMultiplePatients(List<Patient> patients) {
        Map<Patient, String> newPatients = new LinkedHashMap<>();

        for(Patient patient : patients) {
            Optional<Patient> existingPatient = patientRepository.findPatientByPatientNameIgnoreCaseAndDateOfBirth(
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

    /**
     * Delete patient with given id
     * @param patientId
     */
    public void deletePatient(Long patientId) {
        patientRepository.deleteById(patientId);
    }


}
