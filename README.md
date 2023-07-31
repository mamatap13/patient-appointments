# Patient Appointments Management System

The Patient Appointments Management System is a REST-based backend application using
Java and Spring Boot. It provides functionality to retrieve a list of patients and their
scheduled appointments for a selected day. It can also create new patients and schedule 
appointments.

### Setup 
- JAR file located in target folder of project
```
java -jar patient-appointments/target/patient-appointments-0.0.1-SNAPSHOT.jar
```

- Java version: 17.0.8

### Implementation 

- Patient names are case-insensitive
- Patient data is validated to make sure no duplicate names with same date of birth are stored
- Search for patients by id, name, date of birth, and a combination for name and date of birth
- API allows creation of multiple patients

- Search for appointments by day, and by patient name
- On creation of appointment, if patient does not exist, create new patient
- API allows creation of multiple appointments

### API Documentation

- API calls are all preloaded into a Postman collection, available in the .zip file
- Includes Patient APIs and Appointment APIs


- JSON format is required for all API requests.  
- Below is an example of json body required for creating multiple new patients.  
- Date formatting : "yyyy-mm-dd"  
- Time formatting : "hh:mm"

```json
[
    {
        "patientName": "Justin Trudeau",
        "dateOfBirth": "1971-12-25"
    },
    {
        "patientName": "Kamala Harris",
        "dateOfBirth": "1964-10-20"
    },
    {
        "patientName": "Jill Biden",
        "dateOfBirth": "1951-06-03"
    },
    {
        "patientName": "Joe Biden",
        "dateOfBirth": "1942-11-20"
    }
]
```

### Testing

- Performed unit tests at 3 levels: repository, service, controller
- Performed integration (end-to-end) tests on Postman

### Future Improvements

- Current version uses an H2 database (Spring Boot in-memory db), upgrade to PostgreSQL 
- Current version does not have appointment end time, in future adding end time will allow more
detailed checks for overlapping appointments
