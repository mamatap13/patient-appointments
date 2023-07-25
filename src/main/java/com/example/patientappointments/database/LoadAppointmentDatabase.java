package com.example.patientappointments.database;

import com.example.patientappointments.model.Appointment;
import com.example.patientappointments.model.Patient;
import com.example.patientappointments.repository.AppointmentRepository;
import com.example.patientappointments.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class LoadAppointmentDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadAppointmentDatabase.class);

    @Bean
    CommandLineRunner initApptDatabase(AppointmentRepository appointmentRepository) {
        return args -> {
            log.info("Preloading " + appointmentRepository.save(new Appointment(1L,
                    LocalDateTime.of(2023, 10,22, 10, 30))));
            log.info("Preloading " + appointmentRepository.save(new Appointment(2L,
                    LocalDateTime.of(2023, 8,30, 10, 15))));
            log.info("Preloading " + appointmentRepository.save(new Appointment(4L,
                    LocalDateTime.of(2023, 11, 15, 10, 00))));
            log.info("Preloading " + appointmentRepository.save(new Appointment(2L,
                    LocalDateTime.of(2023, 11,15, 10, 30))));
        };
    }



}
