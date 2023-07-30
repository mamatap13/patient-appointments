package com.example.patientappointments.services;

import static org.mockito.Mockito.*;

import com.example.patientappointments.model.Patient;
import com.example.patientappointments.repository.PatientRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;
    @InjectMocks
    private PatientService patientService;

    @Test
    public void testPatientService_GetAllPatients_ReturnSavedPatients() {
        // Arrange
        List<Patient> patients = Arrays.asList(
                new Patient("John Doe", LocalDate.of(1985, 1, 2)),
                new Patient("Jane Smith", LocalDate.of(1995, 1,6))
        );
        patients.get(0).setId(1L);
        patients.get(1).setId(2L);

        when(patientRepository.findAll()).thenReturn(patients);

        // Act
        List<Patient> savedPatients = patientService.getAllPatients();

        // Assert
        Assertions.assertThat(savedPatients).isNotNull();
        Assertions.assertThat(patients.size()).isEqualTo(savedPatients.size());
        Assertions.assertThat(patients).isEqualTo(savedPatients);
    }

    @Test
    public void testPatientService_GetPatientById_ReturnSavedPatient() {
        // Arrange
        Long patientId = 1L;
        Patient patient = new Patient("John Doe", LocalDate.of(1985, 1, 2));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        // Act
        Optional<Patient> foundPatient = patientService.getPatientById(patientId);

        // Assert
        Assertions.assertThat(foundPatient.isPresent()); // Make sure Optional is not empty
        Assertions.assertThat(patient.getId()).isEqualTo(foundPatient.get().getId());
        Assertions.assertThat(patient).isEqualTo(foundPatient.get());
    }

    @Test
    public void testPatientService_GetPatientByName_ReturnSavedPatients() {
        // Arrange
        String patientName = "Jane Smith";
        Patient patient1 = new Patient("Jane Smith", LocalDate.of(1995, 1, 6));
        Patient patient2 = new Patient("John Doe", LocalDate.of(1998, 1, 31));

        when(patientRepository.findByPatientNameIgnoreCase(patientName)).thenReturn(Optional.of(patient1));

        // Act
        Optional<Patient> foundPatient = patientService.getPatientByName(patientName);

        // Assert
        Assertions.assertThat(foundPatient.isPresent()); // Make sure Optional is not empty
        Assertions.assertThat(patient1.getId()).isEqualTo(foundPatient.get().getId());
        Assertions.assertThat(patient1.getPatientName()).isEqualTo(foundPatient.get().getPatientName());
        Assertions.assertThat(patient1.getDateOfBirth()).isEqualTo(foundPatient.get().getDateOfBirth());

    }

    @Test
    public void testPatientService_GetPatientByDateOfBirth_ReturnSavedPatients() {
        // Arrange
        LocalDate dateOfBirth = LocalDate.of(1995, 1, 6);
        Patient patient1 = new Patient("Jane Smith", LocalDate.of(1995, 1, 6));
        Patient patient2 = new Patient("John Doe", LocalDate.of(1998, 1, 31));

        when(patientRepository.findByDateOfBirth(dateOfBirth)).thenReturn(List.of(patient2));

        // Act
        List<Patient> foundPatient = patientService.getPatientByDateOfBirth(dateOfBirth);

        // Assert
        Assertions.assertThat(foundPatient).isNotNull();
        Assertions.assertThat(foundPatient).hasSize(1);
        Assertions.assertThat(patient2.getId()).isEqualTo(foundPatient.get(0).getId());
        Assertions.assertThat(patient2.getPatientName()).isEqualTo(foundPatient.get(0).getPatientName());
        Assertions.assertThat(patient2.getDateOfBirth()).isEqualTo(foundPatient.get(0).getDateOfBirth());
    }

    @Test
    public void testPatientService_GetPatientIdByNameAndDateOfBirth_ReturnSavedPatientId() {
        // Arrange
        String patientName = "Jane Smith";
        LocalDate dateOfBirth = LocalDate.of(1995, 1, 6);
        Patient patient1 = new Patient("Jane Smith", LocalDate.of(1995, 1, 6));

        when(patientRepository.findPatientByPatientNameIgnoreCaseAndDateOfBirth(patientName, dateOfBirth))
                .thenReturn(Optional.of(patient1));

        // Act
        Long foundPatientId = patientService.getPatientIdByNameAndDateOfBirth(patientName, dateOfBirth);

        // Assert
        Assertions.assertThat(foundPatientId).isEqualTo(patient1.getId());

    }


    @Test
    public void testPatientService_GetPatientByNameAndDateOfBirth_ReturnSavedPatients() {
        // Arrange
        String patientName = "Jane Smith";
        LocalDate dateOfBirth = LocalDate.of(1995, 1, 6);
        Patient patient1 = new Patient("Jane Smith", LocalDate.of(1995, 1, 6));

        when(patientRepository.findByPatientNameIgnoreCaseAndDateOfBirth(patientName, dateOfBirth))
                .thenReturn(patient1);

        // Act
        Patient foundPatient = patientService.getPatientByNameAndDateOfBirth(patientName, dateOfBirth);

        // Assert
        Assertions.assertThat(foundPatient).isEqualTo(patient1);
    }
//
    @Test
    public void testPatientService_CreatePatient_ReturnSavedPatient() {
        String patientName = "Jane Smith";
        LocalDate dateOfBirth = LocalDate.of(1995, 1, 6);
        Patient patient1 = new Patient(patientName, dateOfBirth);

        when(patientRepository.save(patient1)).thenReturn(patient1);

        // Act
        Patient createdPatient = patientService.createPatient(patient1);

        // Assert
        Assertions.assertThat(createdPatient).isEqualTo(patient1);
    }

    @Test
    public void testPatientService_CreateMultiplePatients_ReturnSavedPatients() {
        String patientName1 = "Jane Smith";
        LocalDate dateOfBirth1 = LocalDate.of(1995, 1, 6);

        String patientName2 = "John Doe";
        LocalDate dateOfBirth2 = LocalDate.of(1998, 1, 31);

        List<Patient> patients = Arrays.asList(
                new Patient(patientName1, dateOfBirth1),
                new Patient(patientName2, dateOfBirth2)
        );

        // Act
        Map<Patient, String> createdPatients = patientService.createMultiplePatients(patients);

        // Assert
        Assertions.assertThat(createdPatients).isNotNull();
        Assertions.assertThat(createdPatients).hasSize(2);
        Assertions.assertThat(createdPatients.keySet()).containsExactlyInAnyOrderElementsOf(patients);

    }

    @Test
    public void testPatientService_DeletePatient_ReturnEmpty() {
        // Arrange
        Long patientId = 1L;
        Patient patient1 = new Patient("Jane Smith", LocalDate.of(1995, 1, 6));

        // Act
        patientService.deletePatient(patientId);

        // Assert
        verify(patientRepository).deleteById(patientId);
    }
}
