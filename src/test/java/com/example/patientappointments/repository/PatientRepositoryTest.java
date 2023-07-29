package com.example.patientappointments.repository;

import com.example.patientappointments.model.Patient;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PatientRepositoryTest {
    @Autowired
    private PatientRepository patientRepository;

    @Test
    public void testPatientRepository_Save_ReturnSavedPatient() {
        // Arrange
        String patientName = "John Doe";
        LocalDate dateOfBirth = LocalDate.of(1995, 1, 6);
        Patient patient = new Patient(patientName , dateOfBirth);

        // Act
        Patient savedPatient = patientRepository.save(patient);

        // Assert
        Assertions.assertThat(savedPatient).isNotNull();
        Assertions.assertThat(savedPatient.getId()).isNotNull();
        Assertions.assertThat(savedPatient.getPatientName()).isEqualTo(patientName);
        Assertions.assertThat(savedPatient.getDateOfBirth()).isEqualTo(dateOfBirth);
    }

    @Test
    public void testPatientRepository_FindAll_ReturnSavedPatients() {
        // Arrange
        Patient patient1 = new Patient("John Doe" , LocalDate.of(1995,1,6));
        Patient patient2 = new Patient("Jane Doe" , LocalDate.of(1998,1,31));
        Patient patient3 = new Patient("Jack Doe" , LocalDate.of(1995,4,6));

        // Save patients to the database
        patientRepository.save(patient1);
        patientRepository.save(patient2);
        patientRepository.save(patient3);

        // Act
        List<Patient> patients = patientRepository.findAll();

        // Assert
        Assertions.assertThat(patients).isNotNull();
        Assertions.assertThat(patients).hasSize(3);
    }

    @Test
    public void testPatientRepository_FindById_ReturnSavedPatient() {
        // Arrange
        Patient patient = new Patient("John Doe" , LocalDate.of(1995,1,6));
        Patient savedPatient = patientRepository.save(patient);

        // Act
        Optional<Patient> foundPatient = patientRepository.findById(savedPatient.getId());

        // Assert
        Assertions.assertThat(foundPatient).isPresent();
        Assertions.assertThat(foundPatient.get().getId()).isEqualTo(savedPatient.getId());
        Assertions.assertThat(foundPatient.get().getPatientName()).isEqualTo(savedPatient.getPatientName());
        Assertions.assertThat(foundPatient.get().getDateOfBirth()).isEqualTo(savedPatient.getDateOfBirth());
    }

    @Test
    public void testPatientRepository_FindByDateOfBirth_ReturnSavedPatients() {
        // Arrange
        Patient patient1 = new Patient("John Doe" , LocalDate.of(1995,1,6));
        Patient patient2 = new Patient("Jane Doe" , LocalDate.of(1998,1,31));

        patientRepository.save(patient1);
        patientRepository.save(patient2);

        // Act
        List<Patient> foundPatient = patientRepository.findByDateOfBirth(patient1.getDateOfBirth());

        // Assert
        Assertions.assertThat(foundPatient).isNotNull();
        Assertions.assertThat(foundPatient).hasSize(1);
        Assertions.assertThat(foundPatient.get(0).getId()).isEqualTo(patient1.getId());
        Assertions.assertThat(foundPatient.get(0).getPatientName()).isEqualTo(patient1.getPatientName());
        Assertions.assertThat(foundPatient.get(0).getDateOfBirth()).isEqualTo(patient1.getDateOfBirth());
    }

    @Test
    public void testPatientRepository_FindPatientByPatientNameIgnoreCaseAndDateOfBirth_ReturnSavedPatient() {
        // Arrange
        Patient patient1 = new Patient("John Doe" , LocalDate.of(1995,1,6));
        Patient patient2 = new Patient("Jane Doe" , LocalDate.of(1998,1,31));

        patientRepository.save(patient1);
        patientRepository.save(patient2);

        // Act
        Optional<Patient> foundPatient = patientRepository.findPatientByPatientNameIgnoreCaseAndDateOfBirth(
                patient1.getPatientName(), patient1.getDateOfBirth()
        );

        // Assert
        Assertions.assertThat(foundPatient).isNotNull();
        Assertions.assertThat(foundPatient.get().getId()).isEqualTo(patient1.getId());
        Assertions.assertThat(foundPatient.get().getPatientName()).isEqualTo(patient1.getPatientName());
        Assertions.assertThat(foundPatient.get().getDateOfBirth()).isEqualTo(patient1.getDateOfBirth());
    }

    @Test
    public void testPatientRepository_FindByPatientNameIgnoreCaseAndDateOfBirth_ReturnSavedPatients() {
        // Arrange
        Patient patient1 = new Patient("John Doe" , LocalDate.of(1995,1,6));
        Patient patient2 = new Patient("Jane Doe" , LocalDate.of(1998,1,31));

        patientRepository.save(patient1);
        patientRepository.save(patient2);

        // Act
        Patient foundPatient = patientRepository.findByPatientNameIgnoreCaseAndDateOfBirth(
                patient1.getPatientName(), patient1.getDateOfBirth());

        // Assert
        Assertions.assertThat(foundPatient).isNotNull();
        Assertions.assertThat(foundPatient.getId()).isEqualTo(patient1.getId());
        Assertions.assertThat(foundPatient.getPatientName()).isEqualTo(patient1.getPatientName());
        Assertions.assertThat(foundPatient.getDateOfBirth()).isEqualTo(patient1.getDateOfBirth());
    }

    @Test
    public void testPatientRepository_DeletePatient_ReturnEmpty() {
        // Arrange
        Patient patient1 = new Patient("John Doe" , LocalDate.of(1995,1,6));
        Patient patient2 = new Patient("Jane Doe" , LocalDate.of(1998,1,31));

        patientRepository.save(patient1);
        patientRepository.save(patient2);

        // Act
        patientRepository.deleteById(patient1.getId());

        // Assert
        Optional<Patient> deletedPatient = patientRepository.findById(patient1.getId());
        Assertions.assertThat(deletedPatient).isEmpty();

    }
}
