package com.example.patientappointments.controller;

import com.example.patientappointments.model.Appointment;
import com.example.patientappointments.model.Patient;
import com.example.patientappointments.services.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cglib.core.Local;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAppointmentController_GetAllAppointments_ReturnAll() throws Exception {
        // Arrange
        List<Appointment> appointments = Arrays.asList(
                new Appointment(1L, LocalDate.of(2023,8,9),
                        LocalTime.of(9,0)),
                new Appointment(2L, LocalDate.of(2023, 8, 10),
                        LocalTime.of(10,30))
        );

        when(appointmentService.getAllAppointments()).thenReturn(appointments);

        // Act & Assert
        mockMvc.perform(get("http://localhost:8080/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    public void testAppointmentController_GetAppointmentsByPatientName_ReturnAppointments() throws Exception {
        // Arrange
        String patientName = "John Doe";
        LocalDate appointmentDate = LocalDate.of(2023,8,9);
        List<Appointment> appointments = Arrays.asList(
                new Appointment(1L, appointmentDate,
                        LocalTime.of(9,0)),
                new Appointment(2L, LocalDate.of(2023, 8, 10),
                        LocalTime.of(10,30))
        );
        appointments.get(0).setPatientName(patientName);

        List<Appointment> foundAppointments = Arrays.asList(
                new Appointment(1L, appointmentDate, LocalTime.of(9,0))
        );
        foundAppointments.get(0).setPatientName(patientName);

        when(appointmentService.getAppointmentsByPatientName(patientName)).thenReturn(foundAppointments);

        // Act & Assert
        mockMvc.perform(get("http://localhost:8080/api/appointments/patient")
                        .param("patientName","John Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientName", Matchers.is("John Doe")));

    }
    @Test
    public void testAppointmentController_GetAppointmentsByPatientDate_ReturnAppointments() throws Exception {
        // Arrange
        LocalDate appointmentDate = LocalDate.of(2023,8,9);
        List<Appointment> appointments = Arrays.asList(
                new Appointment(1L, appointmentDate,
                        LocalTime.of(9,0)),
                new Appointment(2L, LocalDate.of(2023, 8, 10),
                        LocalTime.of(10,30))
        );

        List<Appointment> foundAppointments = Arrays.asList(
                new Appointment(1L, appointmentDate, LocalTime.of(9,0))
        );
        when(appointmentService.getAppointmentsByDate(appointmentDate)).thenReturn(foundAppointments);

        // Act & Assert
        mockMvc.perform(get("http://localhost:8080/api/appointments/date")
                        .param("appointmentDate","2023-08-09"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].appointmentDate", Matchers.is("2023-08-09")));

    }

    @Test
    public void testAppointmentController_CreateAppointment_ReturnCreated() throws Exception {
        // Arrange
        Map<String, Object> appointmentData = new HashMap<>();
        appointmentData.put("patientName", "John Doe");
        appointmentData.put("dateOfBirth", "1995-01-06");
        appointmentData.put("appointmentDate", "2023-08-09");
        appointmentData.put("appointmentTime", "09:00");
        

        when(appointmentService.createAppointment(anyString(), any(LocalDate.class), any(LocalDate.class), any(LocalTime.class)))
                .thenReturn(new Appointment(1L, LocalDate.of(2023, 8,9), LocalTime.of(9,0)));

        // Act & Assert
        mockMvc.perform(post("http://localhost:8080/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Appointment successfully created"))
                .andExpect(jsonPath("$.data.patientName").value("John Doe"))
                .andExpect(jsonPath("$.data.dateOfBirth").value("1995-01-06"))
                .andExpect(jsonPath("$.data.appointmentDate").value("2023-08-09"))
                .andExpect(jsonPath("$.data.appointmentTime").value("09:00"));
    }


    @Test
    public void testAppointmentController_CreateMultipleAppointments_ReturnCreated() throws Exception{
        // Arrange
        List<Map<String, Object>> appointmentDataList = new ArrayList<>();

        // Add multiple appointment data to the list
        Map<String, Object> appointmentData1 = new HashMap<>();
        appointmentData1.put("patientName", "John Doe");
        appointmentData1.put("dateOfBirth", "1995-01-06");
        appointmentData1.put("appointmentDate", "2023-08-09");
        appointmentData1.put("appointmentTime", "09:00");
        appointmentDataList.add(appointmentData1);

        Map<String, Object> appointmentData2 = new HashMap<>();
        appointmentData2.put("patientName", "Jane Smith");
        appointmentData2.put("dateOfBirth", "1988-03-15");
        appointmentData2.put("appointmentDate", "2023-08-10");
        appointmentData2.put("appointmentTime", "10:30");
        appointmentDataList.add(appointmentData2);

        Map<Map<String, Object>, String> createdAppointments = new HashMap<>();
        createdAppointments.put(appointmentData1, "Appointment successfully created");
        createdAppointments.put(appointmentData2, "Appointment successfully created");
        when(appointmentService.createMultipleAppointments(appointmentDataList)).thenReturn(createdAppointments);

        // Act & Assert
        mockMvc.perform(post("/api/appointments/multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentDataList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)));

    }
}