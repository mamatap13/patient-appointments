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

@RunWith(SpringRunner.class)
@SpringBootTest
public class PatientServiceUnitTest {

    @Autowired
    private PatientService patientService;

    @Test
    public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {
        List<Patient> patients = patientService.getAllPatients();
        Assert.assertEquals(4, patients.size());
    }

}
