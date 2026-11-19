# Student Profile and Skill Development Management System

Spring Boot + JDBC + MySQL project based on the CS336B MSE-II question paper.

## Included requirements
- Student class with encapsulation, constructors, getters and setters.
- Add, retrieve all, search by ID, update scores and skills, and delete.
- Average score, performance category and stronger subject.
- Search by student ID, email or course.
- Merit list sorted by average descending.
- Shared technical skills and programming improvement filter.
- Duplicate student ID/email protection and score validation (0–100).
- Collections: List, HashMap, HashSet, streams and filtering.

## Requirements
JDK 17+, IntelliJ IDEA, MySQL Server and Postman.

## Setup
1. Extract this ZIP and open the folder containing `pom.xml` in IntelliJ.
2. Configure `src/main/resources/application.properties`. Default MySQL username is `root`.
3. Set your MySQL password in `DB_PASSWORD`, or replace `${DB_PASSWORD:}` with your local password.
4. MySQL database `student_management_db` is created automatically if the user has permission. `schema.sql` creates its tables.
5. Run `StudentManagementApplication.java`. Server: `http://localhost:8080`.
6. Import `postman/Student_Profile_Management.postman_collection.json` into Postman.

## Endpoints
| Method | URL | Purpose |
|---|---|---|
| POST | `/api/students` | Add student |
| GET | `/api/students` | Retrieve all |
| GET | `/api/students/101` | Search by ID |
| GET | `/api/students/search?term=CSE` | Search ID/email/course |
| PUT | `/api/students/101` | Update scores and skills |
| DELETE | `/api/students/101` | Delete |
| GET | `/api/students/merit-list` | Merit list |
| GET | `/api/students/same-skills` | Shared skills |
| GET | `/api/students/programming-improvement` | Programming < 50 and Maths >= 75 |

## POST request body example
```json
{
  "studentId": 101,
  "studentName": "Aarav Sharma",
  "email": "aarav@example.com",
  "courseName": "CSE",
  "programmingScore": 88,
  "mathematicsScore": 92,
  "technicalSkills": ["Java", "C++", "SQL"]
}
```

## PUT request body example
```json
{
  "programmingScore": 76,
  "mathematicsScore": 91,
  "technicalSkills": ["Java", "Spring Boot", "MySQL"]
}
```
For PUT, the ID is supplied in the URL. Duplicate ID/email returns HTTP 409; invalid scores return HTTP 400; unknown IDs return HTTP 404.
