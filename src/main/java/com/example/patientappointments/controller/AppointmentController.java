package com.example.patientappointments.controller;

import com.example.patientappointments.model.Appointment;
import com.example.patientappointments.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/byPatientName")
    public ResponseEntity<List<Appointment>> getByPatientName(@RequestParam String patientName) {
        List<Appointment> appointments = appointmentService.getAppointmentsByPatientName(patientName);
        return ResponseEntity.ok(appointments);
    }
}
