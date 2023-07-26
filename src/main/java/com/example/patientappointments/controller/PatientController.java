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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Patient>> getPatientByName(@PathVariable String name) {
        List<Patient> patients = patientService.getPatientByName(name);
        if(patients != null) {
            return ResponseEntity.ok(patients);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

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
    @GetMapping("/name-and-dob")
    public ResponseEntity<List<Patient>> getPatientByNameAndDateOfBirth(
            @RequestParam String name,
            @RequestParam @DateTimeFormat(pattern="MM-dd-yyyy") LocalDate dateOfBirth) {
        List<Patient> patients = patientService.getPatientByNameAndDateOfBirth(name, dateOfBirth);
        if(patients != null) {
            return ResponseEntity.ok(patients);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

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
    @PostMapping("/multiple")
    public ResponseEntity<Map<String, Object>> createMultiplePatients(@RequestBody List<Patient> patients) {
        try{
            List<Patient> newPatients = patientService.createMultiplePatients(patients);
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("message", newPatients.size() + "patients created successfully.");
            response.put("data", newPatients);
            return ResponseEntity.ok(response);
        } catch(PatientException exception) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @DeleteMapping
//    public ResponseEntity<Patient> deletePatient(@PathVariable String name, @PathVariable LocalDateTime dob) {
//        Patient patient = patientService.getPatientByName(name);
//        patientService.deletePatient(patient);
//    }


}
