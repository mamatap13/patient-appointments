package com.example.patientappointments;

import com.example.patientappointments.model.Patient;
import com.example.patientappointments.services.PatientService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PatientServiceUnitTest {

    @Autowired
    private PatientService patientService;

    @Test
    void testGetAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        assertEquals(4, patients.size());
    }

    void testGetPatientById() {
//        Patient patient = patientService.getPatientById();
    }

    void testGetPatientByName() {

    }
}