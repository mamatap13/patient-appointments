package com.example.patientappointments;

import com.example.patientappointments.model.Appointment;
import com.example.patientappointments.services.AppointmentService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppointmentServiceUnitTest {
    @Autowired
    private AppointmentService appointmentService;

    @Test
    public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        Assert.assertEquals(4, appointments.size());
    }
}
