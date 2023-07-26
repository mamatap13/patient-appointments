package com.example.patientappointments.database;

import com.example.patientappointments.model.Patient;
import com.example.patientappointments.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class LoadPatientDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadPatientDatabase.class);

    @Bean
    CommandLineRunner initPatientDatabase(PatientRepository patientRepository) {
        return args -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            log.info("Preloading " + patientRepository.save(new Patient("Justin Trudeau",
                    LocalDate.parse("12-25-1971", formatter))));
            log.info("Preloading " + patientRepository.save(new Patient("Kamala Harris",
                    LocalDate.parse("10-20-1964", formatter))));
            log.info("Preloading " + patientRepository.save(new Patient("Jill Biden",
                    LocalDate.parse("06-03-1951", formatter))));
            log.info("Preloading " + patientRepository.save(new Patient("Joe Biden",
                    LocalDate.parse("11-20-1942", formatter))));
        };
    }

}
