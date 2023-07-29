package com.example.patientappointments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.example.patientappointments.model.Patient;
import com.example.patientappointments.repository.PatientRepository;
import com.example.patientappointments.services.PatientService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@SpringBootTest
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;
    @InjectMocks
    private PatientService patientService;

    @Test
    void testGetAllPatients() {
        // Arrange
        List<Patient> expectedPatients = Arrays.asList(
                new Patient("John Doe", LocalDate.of(1985, 1, 2)),
                new Patient("Jane Smith", LocalDate.of(1995, 1,6))
        );
        expectedPatients.get(0).setId(1L);
        expectedPatients.get(1).setId(2L);

        when(patientRepository.findAll()).thenReturn(expectedPatients);

        // Act
        List<Patient> actualPatients = patientService.getAllPatients();

        // Assert
        assertEquals(expectedPatients, actualPatients);
    }

    @Test
    void testGetPatientById() {
        // Arrange
        Long patientId = 1L;
        Patient expectedPatient = new Patient("John Doe", LocalDate.of(1985, 1, 2));
        expectedPatient.setId(patientId);
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(expectedPatient));

        // Act
        Optional<Patient> actualPatient = patientService.getPatientById(patientId);

        // Assert
        assertTrue(actualPatient.isPresent()); // Make sure Optional is not empty
        assertEquals(expectedPatient, actualPatient.get());
    }

    @Test
    void testGetPatientByName() {
        // Arrange
        String patientName = "janSmith";
        Long patientId = 2L;
        Patient expectedPatient = new Patient("Jane Smith", LocalDate.of(1995, 1, 6));
        expectedPatient.setId(patientId);
        when(patientRepository.findByPatientNameIgnoreCase("Jane Smith")).thenReturn(Optional.of(expectedPatient));

        // Act
        Optional<Patient> actualPatient = patientService.getPatientByName(patientName);

        // Assert
        assertTrue(actualPatient.isPresent()); // Make sure Optional is not empty
        assertEquals(expectedPatient, actualPatient.get());
        System.out.println(expectedPatient);
        System.out.println(actualPatient.get());

    }

    @Test
    void testGetPatientIdByNameAndDateOfBirth() {
        // Arrange
        Long expectedPatientId = 2L;
        String patientName = "John Doe";
        LocalDate dateOfBirth = LocalDate.parse("1995-01-06");

        Patient expectedPatient = new Patient(patientName, dateOfBirth);
        expectedPatient.setId(expectedPatientId);

        when(patientRepository.findPatientByPatientNameIgnoreCaseAndDateOfBirth(patientName, dateOfBirth)).thenReturn(Optional.of(expectedPatient));

        // Act
        Long actualPatientId = patientService.getPatientIdByNameAndDateOfBirth(patientName, dateOfBirth);
        // Assert
        assertEquals(expectedPatient.getId(), actualPatientId);
    }

    @Test
    void testGetPatientByDateOfBirth() {

    }

    @Test
    void testGetPatientByNameAndDateOfBirth() {}

    @Test
    void testCreatePatient() {

    }

    @Test
    void testCreateMultiplePatients() {

    }

    @Test
    void testDeletePatient() {

    }
}
