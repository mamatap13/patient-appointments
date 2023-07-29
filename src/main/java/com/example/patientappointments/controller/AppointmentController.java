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

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    /**
     * Appointments By Patient Name API
     * This api takes in a patient's name and finds the appointments associated with that patient
     *
     * On success: returns a list of appointments for this patient
     *             if no patient name is found, returns an empty list
     *
     * @param patientName
     * @return list of appointments
     */
    @GetMapping("/patient")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatientName(@RequestParam("patientName") String patientName) {
        List<Appointment> appointments = appointmentService.getAppointmentsByPatientName(patientName);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/date")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatientName(@RequestParam("appointmentDate") LocalDate appointmentDate) {
        List<Appointment> appointments = appointmentService.getAppointmentsByDate(appointmentDate);
        return ResponseEntity.ok(appointments);
    }

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

    @PostMapping("/multiple")
    public ResponseEntity<Map<Map<String, Object>, String>> createMultiplePatients(@RequestBody List<Map<String, Object>> appointmentData) {
        Map<Map<String, Object>, String> newAppointments = appointmentService.createMultipleAppointments(appointmentData);
        return ResponseEntity.ok(newAppointments);
    }
}
