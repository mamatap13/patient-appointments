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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class LoadAppointmentDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadAppointmentDatabase.class);

    @Bean
    CommandLineRunner initApptDatabase(AppointmentRepository appointmentRepository) {
        return args -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
            log.info("Preloading " + appointmentRepository.save(new Appointment(1L,
                    LocalDate.parse("10-22-2023", formatter), LocalTime.of(10,30))));
            log.info("Preloading " + appointmentRepository.save(new Appointment(2L,
                    LocalDate.parse("08-30-2023", formatter), LocalTime.of(12, 15))));
            log.info("Preloading " + appointmentRepository.save(new Appointment(4L,
                    LocalDate.parse("11-15-2023", formatter), LocalTime.of(16, 00))));
            log.info("Preloading " + appointmentRepository.save(new Appointment(2L,
                    LocalDate.parse("11-15-2023", formatter), LocalTime.of(13, 30))));
        };
    }



}
