# Patient Appointments Management System

The Patient Appointments Management System is a REST-based backend application using
Java and Spring Boot. It provides functionality to retrieve a list of patients and their
scheduled appointments for a selected day. It can also create new patients and schedule 
appointments.

### Setup Requirements
- JAR file is in project folder

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
