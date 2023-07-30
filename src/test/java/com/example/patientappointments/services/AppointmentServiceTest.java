package com.example.patientappointments.services;

import static org.mockito.Mockito.*;

import com.example.patientappointments.exceptions.AppointmentException;
import com.example.patientappointments.exceptions.PatientException;
import com.example.patientappointments.model.Appointment;
import com.example.patientappointments.model.Patient;
import com.example.patientappointments.repository.AppointmentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientService patientService;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    public void testAppointmentService_GetAllAppointments_ReturnSavedAppointments() {
        // Arrange
        List<Appointment> appointments = Arrays.asList(
                new Appointment(1L, LocalDate.of(2023, 9, 30),
                        LocalTime.of(9,30)),
                new Appointment(2L, LocalDate.of(2023, 8, 15),
                        LocalTime.of(9,15))
        );
        when(appointmentRepository.findAll()).thenReturn(appointments);

        // Act
        List<Appointment> savedAppointments = appointmentService.getAllAppointments();

        // Assert
        Assertions.assertThat(savedAppointments).isNotNull();
        Assertions.assertThat(appointments.size()).isEqualTo(savedAppointments.size());
        Assertions.assertThat(appointments).isEqualTo(savedAppointments);
    }

    @Test
    public void testAppointmentService_GetPatientNameById_ReturnPatientName() {
        // Arrange
        String patientName = "John Doe";
        Appointment appointment = new Appointment(1L,
                LocalDate.of(2023, 9, 30),
                LocalTime.of(9,30));

        when(appointmentRepository.findPatientNameById(appointment.getPatientID())).thenReturn(patientName);

        // Act
        String foundPatientName = appointmentService.getPatientNameById(appointment.getPatientID());

        // Assert
        Assertions.assertThat(foundPatientName).isEqualTo(patientName);
    }

    @Test
    public void testAppointmentService_GetAppointmentsByPatientName_ReturnAppointments() {
        // Arrange
        String patientName = "John Doe";
        Patient patient = new Patient(patientName, LocalDate.of(1995, 1, 6));

        Appointment appointment1 = new Appointment(1L, LocalDate.of(2023, 9, 30),
                                    LocalTime.of(9,30));
        Appointment appointment2 = new Appointment(2L, LocalDate.of(2023, 8, 15),
                                    LocalTime.of(9,15));


        when(appointmentRepository.findAppointmentsByPatientNameIgnoreCase(patientName)).thenReturn(Arrays.asList(appointment1));

        // Act
        List<Appointment> foundAppointments = appointmentService.getAppointmentsByPatientName(patientName);

        // Assert
        Assertions.assertThat(foundAppointments).hasSize(1);
        Assertions.assertThat(appointment1).isEqualTo(foundAppointments.get(0));
        Assertions.assertThat(appointment1.getPatientID()).isEqualTo(foundAppointments.get(0).getPatientID());
    }

    @Test
    public void testAppointmentService_GetAppointmentsByDate_ReturnAppointment() {
        // Arrange
        LocalDate appointmentDate = LocalDate.of(2023, 9, 30);

        Appointment appointment1 = new Appointment(1L, LocalDate.of(2023, 9, 30),
                LocalTime.of(9,30));
        Appointment appointment2 = new Appointment(2L, LocalDate.of(2023, 8, 15),
                LocalTime.of(9,15));
        Appointment appointment3 = new Appointment(2L, LocalDate.of(2023, 9, 30),
                LocalTime.of(9,15));

        when(appointmentRepository.findAppointmentsByDate(appointmentDate)).thenReturn(Arrays.asList(appointment1, appointment3));

        // Act
        List<Appointment> foundAppointments = appointmentService.getAppointmentsByDate(appointmentDate);

        // Assert
        Assertions.assertThat(foundAppointments).hasSize(2);
        Assertions.assertThat(appointment1).isEqualTo(foundAppointments.get(0));
        Assertions.assertThat(appointment3).isEqualTo(foundAppointments.get(1));
        Assertions.assertThat(appointmentDate).isEqualTo(foundAppointments.get(0).getAppointmentDate());
    }

    @Test
    public void testAppointmentService_CreateAppointment_ReturnSavedAppointment() {
        // Arrange
        String patientName = "John Doe";
        LocalDate dateOfBirth = LocalDate.of(1995, 1, 6);
        LocalDate appointmentDate = LocalDate.of(2023, 9, 30);
        LocalTime appointmentTime = LocalTime.of(9,30);


        // Mocking patientService to return patient id
        Long patientId = 1L;
        when(patientService.getPatientIdByNameAndDateOfBirth(patientName, dateOfBirth)).thenReturn(patientId);

        // Mocking appointRepository to return saved appointment
        Appointment appointment = new Appointment(patientId, appointmentDate, appointmentTime);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // Act
        Appointment createdAppointment = appointmentService.createAppointment(
                patientName, dateOfBirth, appointmentDate, appointmentTime);

        // Assert
        Assertions.assertThat(createdAppointment).isNotNull();
        Assertions.assertThat(patientId).isEqualTo(appointment.getPatientID());
        Assertions.assertThat(appointmentDate).isEqualTo(appointment.getAppointmentDate());
        Assertions.assertThat(appointmentTime).isEqualTo(appointment.getAppointmentTime());
    }

    @Test
    public void testAppointmentService_AppointmentNotAvailable() {
        // Arrange
        LocalDate appointmentDate = LocalDate.of(2023, 9, 30);
        LocalTime appointmentTime = LocalTime.of(9,30);

        // Mock appointmentRepository to return existing appointment with given date and time
        when(appointmentRepository.findAppointmentByDateAndTime(appointmentDate, appointmentTime)).thenReturn(new Appointment());

        // Act
        // Assert
        assertThrows(AppointmentException.class, () ->
                appointmentService.createAppointment("John Doe", LocalDate.of(1995, 1, 6),
                        appointmentDate, appointmentTime)
                );

    }

    @Test
    public void testAppointmentService_PatientDoesNotExist() {
        // Assert
        String patientName = "John Doe";
        LocalDate dateOfBirth = LocalDate.of(1995, 1, 6);
        LocalDate appointmentDate = LocalDate.of(2023, 9, 30);
        LocalTime appointmentTime = LocalTime.of(9,30);


        // Mock patientService to throw exception, since patient id does not exist
        when(patientService.getPatientIdByNameAndDateOfBirth(patientName, dateOfBirth))
                .thenThrow(new PatientException("Patient not found"));

        // Mock patientService creating a new patient
        Patient newPatient = new Patient(patientName, dateOfBirth);
        when(patientService.createPatient(any(Patient.class))).thenReturn(newPatient);

        // Mock appointmentRepository returning new appointment
        Appointment appointment = new Appointment(newPatient.getId(), appointmentDate, appointmentTime);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // Act
        Appointment createdAppointment = appointmentService.createAppointment(
                patientName, dateOfBirth, appointmentDate, appointmentTime);

        // Assert
        Assertions.assertThat(createdAppointment).isNotNull();
        Assertions.assertThat(newPatient.getId()).isEqualTo(createdAppointment.getPatientID());
        Assertions.assertThat(appointmentDate).isEqualTo(createdAppointment.getAppointmentDate());
        Assertions.assertThat(appointmentTime).isEqualTo(createdAppointment.getAppointmentTime());
    }

    @Test
    public void testAppointmentService_CreateMultipleAppointments_ReturnAllAppointments() {
        // Arrange
        List<Map<String, Object>> appointmentRequests = Arrays.asList(
                Map.of("patientName", "John Doe",
                        "dateOfBirth", "1995-01-06",
                        "appointmentDate", "2023-09-30",
                        "appointmentTime", "09:30"),
                Map.of("patientName", "Jane Smith",
                        "dateOfBirth", "1995-01-06",
                        "appointmentDate", "2023-10-30",
                        "appointmentTime", "09:30"),
                Map.of("patientName", "Jane Doe",
                        "dateOfBirth", "1998-01-31",
                        "appointmentDate", "2023-09-30",
                        "appointmentTime", "09:30")
        );

        Appointment appointment1 = new Appointment(1L, LocalDate.of(2023, 9, 30),
                LocalTime.of(9, 30));
        Appointment appointment2 = new Appointment(2L, LocalDate.of(2023, 10, 30),
                LocalTime.of(9, 30));


        // Mock appointmentRepository checking for appointment
        when(appointmentRepository.findAppointmentByDateAndTime(LocalDate.of(2023, 9, 30),
                LocalTime.of(9, 30))).thenReturn(appointment1);
        when(appointmentRepository.findAppointmentByDateAndTime(LocalDate.of(2023, 10, 30),
                LocalTime.of(9, 30))).thenReturn(appointment2);
        when(appointmentRepository.findAppointmentByDateAndTime(LocalDate.of(2023, 9, 30),
                LocalTime.of(9, 30))).thenReturn(null);


        // Act
        Map<Map<String, Object>, String> results = appointmentService.createMultipleAppointments(appointmentRequests);

        // Assert
        Assertions.assertThat(results).hasSize(3);
    }

    @Test
    public void testAppointmentService_IsAppointmentAvailable_ReturnBoolean() {
        // Arrange
        LocalDate appointmentDate = LocalDate.of(2023, 9, 30);
        LocalTime appointmentTime = LocalTime.of(9,30);
        when(appointmentRepository.findAppointmentByDateAndTime(appointmentDate, appointmentTime)).thenReturn(new Appointment());

        // Act
        boolean isAppointmentAvailable =  appointmentService.isAppointmentAvailable(appointmentDate, appointmentTime);

        // Assert
        assertFalse(isAppointmentAvailable); // Expecting appointment is not available
    }
}