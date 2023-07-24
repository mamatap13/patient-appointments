package com.example.patientappointments.services;

import com.example.patientappointments.model.Appointment;
import com.example.patientappointments.repository.AppointmentRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();

    }

    public List<Appointment> getAppointmentsByPatientName(String patientName) {
        return appointmentRepository.findByPatientName(patientName);
    }

}
