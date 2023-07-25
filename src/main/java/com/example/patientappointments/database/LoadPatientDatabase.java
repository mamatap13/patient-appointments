package com.example.patientappointments.database;

import com.example.patientappointments.model.Patient;
import com.example.patientappointments.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class LoadPatientDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadPatientDatabase.class);

    @Bean
    CommandLineRunner initPatientDatabase(PatientRepository patientRepository) {
        return args -> {
            log.info("Preloading " + patientRepository.save(new Patient("George Washington",
                    LocalDate.of(1732, 2,22))));
            log.info("Preloading " + patientRepository.save(new Patient("John Adams",
                    LocalDate.of(1735, 10,30))));
            log.info("Preloading " + patientRepository.save(new Patient("Thomas Jefferson",
                    LocalDate.of(1743, 4,13))));
            log.info("Preloading " + patientRepository.save(new Patient("Joe Biden",
                    LocalDate.of(1942, 11,20))));
        };
    }

}
