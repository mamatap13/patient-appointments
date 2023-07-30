package com.example.patientappointments.services;

import com.example.patientappointments.exceptions.AppointmentException;
import com.example.patientappointments.exceptions.PatientException;
import com.example.patientappointments.model.Appointment;
import com.example.patientappointments.model.Patient;
import com.example.patientappointments.repository.AppointmentRepository;
import com.example.patientappointments.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final PatientService patientService;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository,
                              PatientService patientService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.patientService = patientService;
    }

    /**
     * Get all appointments from database
     * @return list of appointments
     */
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        for(Appointment appointment : appointments) {
            String patientName = getPatientNameById(appointment.getPatientID());
            appointment.setPatientName(patientName);
        }
        return appointments;

    }

    /**
     * Get patient name associated with id
     * @param id
     * @return patient
     */
    public String getPatientNameById(Long id) {
        return appointmentRepository.findPatientNameById(id);
    }

    /**
     * Get appointments by patient name (Appointment(patientid, apptDate, apptTime)
     * @param patientName
     * @return list of appointments
     */
    public List<Appointment> getAppointmentsByPatientName(String patientName) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByPatientNameIgnoreCase(patientName);
        for(Appointment appointment : appointments) {
            String name = getPatientNameById(appointment.getPatientID());
            appointment.setPatientName(name);
        }
        return appointments;
    }

    /**
     * Get all appointmets on given date
     * @param appointmentDate
     * @return list of appointments
     */
    public List<Appointment> getAppointmentsByDate(LocalDate appointmentDate) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDate(appointmentDate);
        for(Appointment appointment : appointments) {
            String name = getPatientNameById(appointment.getPatientID());
            appointment.setPatientName(name);
        }
        return appointments;
    }

    /**
     * Create a new appointment, if date and time are available, otherwise throw exception
     * If patient does not exist, create new patient
     * @param patientName
     * @param dateOfBirth
     * @param appointmentDate
     * @param appointmentTime
     * @return saved appointment
     */
    public Appointment createAppointment(String patientName, LocalDate dateOfBirth, LocalDate appointmentDate, LocalTime appointmentTime) {
        Appointment existingAppointment = appointmentRepository.findAppointmentByDateAndTime(appointmentDate, appointmentTime);
        if(existingAppointment != null) {
            throw new AppointmentException("Appointment not available");
        }

        Long patientId;
        try {
            patientId = patientService.getPatientIdByNameAndDateOfBirth(patientName, dateOfBirth);

        } catch (PatientException exception) {
            Patient newPatient = patientService.createPatient(new Patient(patientName, dateOfBirth));
            patientId = newPatient.getId();
        }
        Appointment appointment = new Appointment(patientId, appointmentDate, appointmentTime);
        return appointmentRepository.save(appointment);
    }

    /**
     * Create multiple appointments, check availability, check existing patients
     * Return map of all requested appointments with message indicating success or failure of appt creation
     * @param appointmentRequests
     * @return map
     */
    public Map<Map<String, Object>, String> createMultipleAppointments(List<Map<String, Object>> appointmentRequests) {
        Map<Map<String, Object>, String> requestedAppointments = new LinkedHashMap<>();

        for(Map<String, Object> request : appointmentRequests) {

            String patientName = (String) request.get("patientName");
            LocalDate dateOfBirth;
            LocalDate appointmentDate;
            LocalTime appointmentTime;
            try{
                dateOfBirth = LocalDate.parse((String) request.get("dateOfBirth"), dateFormatter);
                appointmentDate = LocalDate.parse((String) request.get("appointmentDate"), dateFormatter);
                appointmentTime = LocalTime.parse((String) request.get("appointmentTime"), timeFormatter);
            } catch(DateTimeParseException exception) {
                requestedAppointments.put(request, "Invalid date or time format");
                continue;
            }

            if (isAppointmentAvailable(appointmentDate, appointmentTime)) {
                Long patientId;
                try {
                    patientId = patientService.getPatientIdByNameAndDateOfBirth(patientName, dateOfBirth);
                } catch (PatientException exception) {
                    Patient newPatient = patientService.createPatient(new Patient(patientName, dateOfBirth));
                    patientId = newPatient.getId();
                }
                Appointment newAppointment = new Appointment(patientId, appointmentDate, appointmentTime);
                appointmentRepository.save(newAppointment);
                requestedAppointments.put(request, "Appointment for " + patientName + " created successfully");
            } else {
                requestedAppointments.put(request, "Appointment for " + patientName + " not available");
            }
        }
        return requestedAppointments;
    }

    /**
     * Check appointment availability
     * @param appointmentDate
     * @param appointmentTime
     * @return boolean
     */
    public boolean isAppointmentAvailable(LocalDate appointmentDate, LocalTime appointmentTime) {
        return appointmentRepository.findAppointmentByDateAndTime(appointmentDate, appointmentTime) == null;
    }

}
