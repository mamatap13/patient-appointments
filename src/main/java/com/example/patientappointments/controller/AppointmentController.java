package com.example.patientappointments.controller;

import com.example.patientappointments.exceptions.AppointmentException;
import com.example.patientappointments.exceptions.PatientException;
import com.example.patientappointments.model.Appointment;
import com.example.patientappointments.model.Patient;
import com.example.patientappointments.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Get All Appointments API
     * Find all appointments in the database
     *
     * @return list of all appointments
     */
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    /**
     * Appointments By Patient Name API
     * Takes in a patient's name and finds the appointments associated with that patient
     *
     * On success: returns a list of appointments for given patient
     *             if no patient name is found, returns an empty list
     *
     * api/appointments/patient?patientName=Kamala%20Harris
     *
     * @param patientName
     * @return list of appointments
     */
    @GetMapping("/patient")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatientName(@RequestParam("patientName") String patientName) {
        List<Appointment> appointments = appointmentService.getAppointmentsByPatientName(patientName);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Get Appointments By Patient Name API
     * Takes in a patient's name and find all appointments associated with that name
     * May have multiple patients with the same name
     *
     * api/date?appointmentDate="2023-09-30"
     *
     * @param appointmentDate
     * @return list of appointments
     */
    @GetMapping("/date")
    public ResponseEntity<List<Appointment>> getAppointmentsByDate(@RequestParam("appointmentDate") LocalDate appointmentDate) {
        List<Appointment> appointments = appointmentService.getAppointmentsByDate(appointmentDate);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Create Appointments API
     * Takes a map of data in JSON format, creates a new appointment
     * Catch DateTimeParseException if invalid date or time format
     * Catch AppointmentException if date and time are not available
     * Catch all exception
     *
     * Return map with appointmentData and success/failure message
     * @param appointmentData
     * @return map
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createAppointment(@RequestBody Map<String, Object> appointmentData) {
        try {
            String patientName = (String) appointmentData.get("patientName");
            LocalDate patientDateOfBirth = LocalDate.parse((String) appointmentData.get("dateOfBirth"));
            LocalDate appointmentDate = LocalDate.parse((String) appointmentData.get("appointmentDate"));
            LocalTime appointmentTime = LocalTime.parse((String) appointmentData.get("appointmentTime"));
            Appointment newAppointment = appointmentService.createAppointment(patientName, patientDateOfBirth, appointmentDate, appointmentTime);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Appointment successfully created");
            response.put("data", appointmentData);
            return ResponseEntity.ok(response);
        } catch(DateTimeParseException exception) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Invalid date or time format");
            response.put("data", appointmentData);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch(AppointmentException exception) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", exception.getMessage());
            response.put("data", appointmentData);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch(Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("data", appointmentData);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    /**
     * Create Multiple Patients API
     * Takes in a list of data in JSON format in request body
     * Catch exceptions in appointmentService.java
     * Return map of new appointments with messages of success/failure
     *
     * @param appointmentData
     * @return map of data
     */
    @PostMapping("/multiple")
    public ResponseEntity<Map<Map<String, Object>, String>> createMultipleAppointments(@RequestBody List<Map<String, Object>> appointmentData) {
        Map<Map<String, Object>, String> newAppointments = appointmentService.createMultipleAppointments(appointmentData);
        return ResponseEntity.ok(newAppointments);
    }
}
