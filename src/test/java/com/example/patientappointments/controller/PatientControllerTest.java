package com.example.patientappointments.controller;

import com.example.patientappointments.model.Patient;
import com.example.patientappointments.services.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Test
    public void testPatientController_getAllPatients_ReturnAll() throws Exception{
        // Arrange
        List<Patient> patients = Arrays.asList(
                new Patient("John Doe", LocalDate.of(1995, 1, 6)),
                new Patient("Jane Smith", LocalDate.of(1998, 1, 31))
        );

        when(patientService.getAllPatients()).thenReturn(patients);

        // Act & Assert
        mockMvc.perform(get("http://localhost:8080/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].patientName", Matchers.is("John Doe")))
                .andExpect(jsonPath("$[1].patientName", Matchers.is("Jane Smith")))
                .andExpect(jsonPath("$[0].dateOfBirth", Matchers.is("1995-01-06")))
                .andExpect(jsonPath("$[1].dateOfBirth", Matchers.is("1998-01-31")));
    }

    @Test
    public void testPatientController_GetPatientById_ReturnPatient() throws Exception {
        // Arrange
        Long patientId = 1L;
        Patient patient = new Patient("John Doe", LocalDate.of(1995, 1, 6));
        patient.setId(1L);

        when(patientService.getPatientById(patient.getId())).thenReturn(Optional.of(patient));

        // Act & Assert
        mockMvc.perform(get("http://localhost:8080/api/patients/id/{id}", patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.patientName", Matchers.is("John Doe")))
                .andExpect(jsonPath("$.dateOfBirth", Matchers.is("1995-01-06")));
    }
    @Test
    public void testPatientController_GetPatientById_ReturnPatientNotFound() throws Exception {
        // Arrange
        Long patientId = 1L;
        Patient patient = new Patient("John Doe", LocalDate.of(1995, 1, 6));
        patient.setId(1L);

        when(patientService.getPatientById(patient.getId())).thenReturn(Optional.of(patient));

        // Act & Assert
        mockMvc.perform(get("http://localhost:8080/api/patients/id/{id}", 2))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testPatientController_GetPatientByName_ReturnPatient() throws Exception{
        // Arrange
        Long patientId = 1L;
        String patientName = "John Doe";
        LocalDate dateOfBirth = LocalDate.of(1995, 1, 6);
        Patient patient = new Patient(patientName, dateOfBirth);
        patient.setId(1L);

        when(patientService.getPatientByName(patientName)).thenReturn(Optional.of(patient));

        // Act & Assert
        mockMvc.perform(get("http://localhost:8080/api/patients/name/{name}", patientName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.patientName", Matchers.is("John Doe")))
                .andExpect(jsonPath("$.dateOfBirth", Matchers.is("1995-01-06")));
    }

    @Test
    public void testPatientController_GetPatientByName_ReturnPatientNotFound() throws Exception{
        // Arrange
        Long patientId = 1L;
        String patientName = "John Doe";
        LocalDate dateOfBirth = LocalDate.of(1995, 1, 6);
        Patient patient = new Patient(patientName, dateOfBirth);
        patient.setId(1L);

        when(patientService.getPatientByName(patientName)).thenReturn(Optional.of(patient));

        // Act & Assert
        mockMvc.perform(get("http://localhost:8080/api/patients/name/{name}", "Jane Smith"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPatientController_GetPatientByDateOfBirth_ReturnPatient() throws Exception{
        // Arrange
        Long patientId = 1L;
        String patientName = "John Doe";
        LocalDate dateOfBirth = LocalDate.of(1995, 1, 6);
        Patient patient = new Patient(patientName, dateOfBirth);
        patient.setId(patientId);


        List<Patient> results = Collections.singletonList(patient);
        when(patientService.getPatientByDateOfBirth(dateOfBirth)).thenReturn(results);

        // Act & Assert
        mockMvc.perform(get("http://localhost:8080/api/patients/dob").param("dateOfBirth","01-06-1995"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].patientName", Matchers.is("John Doe")))
                .andExpect(jsonPath("$[0].dateOfBirth", Matchers.is("1995-01-06")));

    }

    @Test
    public void testPatientController_GetPatientByDateOfBirth_ReturnPatientNotFound() throws Exception{
        // Arrange
        LocalDate dateOfBirth = LocalDate.of(1995, 1, 6);

        when(patientService.getPatientByDateOfBirth(dateOfBirth)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("http://localhost:8080/api/patients/dob").param("dateOfBirth","01-06-1995"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testPatientController_GetPatientByNameAndDateOfBirth_ReturnPatient() throws Exception{
        // Arrange
        Long patientId = 1L;
        String patientName = "John Doe";
        LocalDate dateOfBirth = LocalDate.of(1995, 1, 6);
        Patient patient = new Patient(patientName, dateOfBirth);
        patient.setId(patientId);


        when(patientService.getPatientByNameAndDateOfBirth(patientName, dateOfBirth)).thenReturn(patient);

        // Act & Assert
        mockMvc.perform(get("http://localhost:8080/api/patients/name-and-dob")
                        .param("name", "John Doe")
                        .param("dateOfBirth","01-06-1995"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id", Matchers.is(1)))
                        .andExpect(jsonPath("$.patientName", Matchers.is("John Doe")))
                        .andExpect(jsonPath("$.dateOfBirth", Matchers.is("1995-01-06")));
    }

    @Test
    public void testPatientController_GetPatientByNameAndDateOfBirth_ReturnPatientNotFound() throws Exception{
        // Arrange

        String patientName = "John Doe";
        LocalDate dateOfBirth = LocalDate.of(1995, 1, 6);

        when(patientService.getPatientByNameAndDateOfBirth(patientName, dateOfBirth)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("http://localhost:8080/api/patients/name-and-dob")
                        .param("name", "John Doe")
                        .param("dateOfBirth","01-06-1995"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPatientController_CreatePatient_ReturnCreated() throws Exception{
        // Arrange
        Patient patient = new Patient("John Doe", LocalDate.of(1995, 1, 6));
        patient.setId(1L);


        when(patientService.createPatient(any(Patient.class))).thenReturn(patient);

        // Act & Assert
        mockMvc.perform(post("http://localhost:8080/api/patients")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", Matchers.is(1)))
                .andExpect(jsonPath("$.data.patientName", Matchers.is("John Doe")))
                .andExpect(jsonPath("$.data.dateOfBirth", Matchers.is("1995-01-06")));
    }

    @Test
    public void testPatientController_CreateMultiplePatients_ReturnList() throws Exception{
        // Arrange
        List<Patient> patients = Arrays.asList(
                new Patient("John Doe", LocalDate.of(1995, 1, 6)),
                new Patient("Jane Smith", LocalDate.of(1998, 1, 31))
        );

        Map<Patient, String> patientsMap = new HashMap<>();
        patientsMap.put(patients.get(0), "Created successfully");
        patientsMap.put(patients.get(1), "Created successfully");

        when(patientService.createMultiplePatients(any(List.class))).thenReturn(patientsMap);

        // Act & Assert
        mockMvc.perform(post("http://localhost:8080/api/patients/multiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patients)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)));
    }

    @Test
    public void testPatientController_DeletePatient() throws Exception{
        Long patientId = 1L;
        Patient patient = new Patient("John Doe", LocalDate.of(1995, 1, 6));
        patient.setId(patientId);
        doNothing().when(patientService).deletePatient(patientId);

        mockMvc.perform(delete("http://localhost:8080/api/patients/{patientId}", patientId))
                .andExpect(status().isOk());
    }
}