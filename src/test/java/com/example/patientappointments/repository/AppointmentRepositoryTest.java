package com.example.patientappointments.repository;

import com.example.patientappointments.model.Appointment;
import com.example.patientappointments.model.Patient;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AppointmentRepositoryTest {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;


    @Test
    public void testAppointmentRepository_Save_ReturnSavedAppointments() {
        // Arrange
        Long patientId = 1L;
        LocalDate appointmentDate = LocalDate.of(2023,8,30);
        LocalTime appointmentTime = LocalTime.of(11,30);
        Appointment appointment = new Appointment(patientId, appointmentDate, appointmentTime);

        // Act
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Assert
        Assertions.assertThat(savedAppointment).isNotNull();
        Assertions.assertThat(savedAppointment.getId()).isEqualTo(appointment.getId());
        Assertions.assertThat(savedAppointment.getAppointmentDate()).isEqualTo(appointment.getAppointmentDate());
        Assertions.assertThat(savedAppointment.getAppointmentTime()).isEqualTo(appointment.getAppointmentTime());

    }
    @Test
    public void testAppointmentRepository_FindAll_ReturnSavedAppointments() {
        // Arrange
        Appointment appointment1 = new Appointment(1L,
                LocalDate.of(2023,8,30),
                LocalTime.of(11,30));
        Appointment appointment2 = new Appointment(2L,
                LocalDate.of(2023,8,31),
                LocalTime.of(10,00));

        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);

        // Act
        List<Appointment> foundAppointments = appointmentRepository.findAll();

        // Assert
        Assertions.assertThat(foundAppointments).isNotNull();
        Assertions.assertThat(foundAppointments).hasSize(2);
    }

    @Test
    public void testAppointmentRepository_FindAppointmentsByPatientNameIgnoreCase_ReturnSavedAppointments() {
        // Arrange
        String patientName = "John Doe";

        Patient patient = new Patient("John Doe", LocalDate.of(1995, 1, 6));
        patientRepository.save(patient);

        Appointment appointment1 = new Appointment(1L,
                LocalDate.of(2023,8,30),
                LocalTime.of(11,30));
        appointmentRepository.save(appointment1);

        Appointment appointment2 = new Appointment(2L,
                LocalDate.of(2023,8,31),
                LocalTime.of(10,00));
        appointmentRepository.save(appointment2);

        // Act
        List<Appointment> foundAppointments = appointmentRepository.findAppointmentsByPatientNameIgnoreCase(patientName);

        // Assert
        Assertions.assertThat(foundAppointments).hasSize(1);
        Assertions.assertThat(foundAppointments.get(0).getPatientID()).isEqualTo(patient.getId());

    }

    @Test
    public void testAppointmentRepository_FindAppointmentsByDate_ReturnSavedAppointments(){
        // Arrange
        LocalDate appointmentDate = LocalDate.of(2023, 8, 30);
        Appointment appointment1 = new Appointment(1L,
                LocalDate.of(2023,8,30),
                LocalTime.of(11,30));
        appointmentRepository.save(appointment1);

        Appointment appointment2 = new Appointment(2L,
                LocalDate.of(2023,8,31),
                LocalTime.of(10,00));
        appointmentRepository.save(appointment2);

        // Act
        List<Appointment> foundAppointments = appointmentRepository.findAppointmentsByDate(appointmentDate);

        // Assert
        Assertions.assertThat(foundAppointments).hasSize(1);
        Assertions.assertThat(foundAppointments.get(0).getId()).isEqualTo(appointment1.getId());
        Assertions.assertThat(foundAppointments.get(0).getAppointmentDate()).isEqualTo(appointment1.getAppointmentDate());
        Assertions.assertThat(foundAppointments.get(0).getAppointmentTime()).isEqualTo(appointment1.getAppointmentTime());

    }

    @Test
    public void testAppointmentRepository_FindAppointmentByDateAndTime_ReturnSavedAppointment(){
        // Arrange
        LocalDate appointmentDate = LocalDate.of(2023, 8, 30);
        LocalTime appointmentTime = LocalTime.of(11, 30);
        Appointment appointment1 = new Appointment(1L,
                LocalDate.of(2023,8,30),
                LocalTime.of(11,30));
        appointmentRepository.save(appointment1);

        Appointment appointment2 = new Appointment(2L,
                LocalDate.of(2023,8,31),
                LocalTime.of(10,00));
        appointmentRepository.save(appointment2);

        // Act
        Appointment foundAppointments = appointmentRepository.findAppointmentByDateAndTime(
                appointmentDate, appointmentTime);

        // Assert
        Assertions.assertThat(foundAppointments).isNotNull();
        Assertions.assertThat(foundAppointments.getId()).isEqualTo(appointment1.getId());
        Assertions.assertThat(foundAppointments.getAppointmentDate()).isEqualTo(appointment1.getAppointmentDate());
        Assertions.assertThat(foundAppointments.getAppointmentTime()).isEqualTo(appointment1.getAppointmentTime());
    }
}
