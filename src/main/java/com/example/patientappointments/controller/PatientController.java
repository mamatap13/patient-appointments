package com.example.patientappointments.controller;

import com.example.patientappointments.exceptions.PatientException;
import com.example.patientappointments.model.Patient;
import com.example.patientappointments.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Get All Patients API
     * Find all patients in the database
     *
     * @return list of all patients
     */
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    /**
     * Get Patients By Id API
     * Takes in a patient id and finds patient associated with that id
     *
     * api/patients/id/1
     *
     * @param id
     * @return Patient
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patient = patientService.getPatientById(id);

        return patient.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get Patient By Name API
     * Takes in a patient name and find patient with that name
     * (may have multiple patients with same name)
     *
     * api/patients/name/Kamala%20Harris
     *
     * @param name
     * @return list of patients
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Optional<Patient>> getPatientByName(@PathVariable String name) {
        Optional<Patient> patients = patientService.getPatientByName(name);
        if(patients.isPresent()) {
            return ResponseEntity.ok(patients);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get Patient By Date of Birth API
     * Takes in a date of birth, returns patient if found
     * Returns list of patients if present, else empty list
     *
     * api/patients/dob?dateOfBirth=01-06-1995
     *
     * @param dateOfBirth
     * @return list of patients
     */
    @GetMapping("/dob")
    public ResponseEntity<List<Patient>> getPatientByDateOfBirth(
            @RequestParam @DateTimeFormat(pattern="MM-dd-yyyy") LocalDate dateOfBirth) {
        List<Patient> patients = patientService.getPatientByDateOfBirth(dateOfBirth);
        if(patients != null) {
            return ResponseEntity.ok(patients);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get Patient By Name and Date of Birth API
     * Takes in a name and dateOfBirth, if found returns patient
     *
     * api/patients/name-and-dob?name=Kamala%20Harris&dateOfBirth=10-20-1964
     *
     * @param name
     * @param dateOfBirth
     * @return patient
     */
    @GetMapping("/name-and-dob")
    public ResponseEntity<Patient> getPatientByNameAndDateOfBirth(
            @RequestParam String name,
            @RequestParam @DateTimeFormat(pattern="MM-dd-yyyy") LocalDate dateOfBirth) {
        Patient patient = patientService.getPatientByNameAndDateOfBirth(name, dateOfBirth);
        if(patient != null) {
            return ResponseEntity.ok(patient);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create Patient API
     * Takes in patient data in JSON format
     * Catch PatientException if patient already exists
     * Call all exception for all other problems
     * Return new patient data with success/failure message
     *
     * api/patients
     *
     * @param patient
     * @return map of new patient and success/failure message
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPatient(@RequestBody Patient patient) {
        try{
            Patient newPatient = patientService.createPatient(patient);
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Patient created successfully.");
            response.put("data", newPatient);
            return ResponseEntity.ok(response);
        } catch(PatientException exception) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", exception.getMessage());
            response.put("data", patient);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch(Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "An error has occurred.");
            response.put("data", patient);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Create Multiple Patients API
     * Takes in a list of patient data in JSON format
     * Results all requested patients with success/failure message
     *
     * api/patients/multiple
     *
     * @param patients
     * @return list of patients
     */
    @PostMapping("/multiple")
    public ResponseEntity<Map<Patient, String>> createMultiplePatients(@RequestBody List<Patient> patients) {
        Map<Patient, String> newPatients = patientService.createMultiplePatients(patients);
        return ResponseEntity.ok(newPatients);
    }

    /**
     * Delete Patient API
     * Delete patient associated with given patientId
     *
     * api/patients/1
     *
     * @param patientId
     * @return
     */
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long patientId) {
        try{
            patientService.deletePatient(patientId);
            return ResponseEntity.ok().build();
        } catch (PatientException patientException) {
            return ResponseEntity.notFound().build();
        }
    }


}
