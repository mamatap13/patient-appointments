package com.example.patientappointments.services;

import com.example.patientappointments.model.Patient;
import com.example.patientappointments.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();

    }

    public List<Patient> findByName(String name) {
        return patientRepository.findByName(name);
    }
}
