# HireFlow Backend - REST API

## Overview

HireFlow Backend is a Spring Boot REST API that manages the complete recruitment workflow:
- **Authentication**: User registration & JWT-based login
- **Candidate Management**: Profile creation, interview bookings
- **Interview Management**: Slot creation, feedback submission
- **Recruiter Dashboard**: Candidate pipeline tracking & hiring decisions

**Base URL**: `http://localhost:8080/api/v1`

---

## Prerequisites

```
✓ Java 17+   (https://www.oracle.com/java/technologies/downloads/)
✓ Maven 3.6+ (https://maven.apache.org/download.cgi)
✓ MySQL 8.0+ (https://dev.mysql.com/downloads/mysql/)
```

**Verify Installation**:
```bash
java -version
mvn -version
mysql --version
```

---

## Setup

### 1. Create Database
```bash
mysql -u root -p

# In MySQL:
CREATE DATABASE hireflow_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EXIT;
```

### 2. Configure Application
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hireflow_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 3. Install Dependencies
```bash
mvn clean install
```

---

## Running

### Development
```bash
mvn spring-boot:run
```

### Production Build
```bash
mvn clean package
java -jar target/hireflow-backend-0.0.1-SNAPSHOT.jar
```

**Verify**: Open `http://localhost:8080/api/v1/swagger-ui.html`

---

## API Documentation

### Interactive Docs (Swagger UI)
```
http://localhost:8080/api/v1/swagger-ui.html
```

### Key Endpoints

**Authentication** (No token required)
```
POST   /auth/register
POST   /auth/login
```

**Candidate** (Requires CANDIDATE role)
```
POST   /candidates/{userId}/profile
GET    /candidates/slots
POST   /candidates/bookings
PUT    /candidates/bookings/reschedule
DELETE /candidates/bookings/{bookingId}
GET    /candidates/{candidateId}/bookings
```

**Interview** (Requires INTERVIEWER role)
```
POST   /interviews/slots
PUT    /interviews/slots/{slotId}
DELETE /interviews/slots/{slotId}
GET    /interviews/{interviewerId}/slots
GET    /interviews/{interviewerId}/bookings
POST   /interviews/feedback
```

**Recruiter** (Requires RECRUITER role)
```
GET    /recruiter/dashboard
GET    /recruiter/candidates
GET    /recruiter/candidates/pending
GET    /recruiter/candidates/selected
GET    /recruiter/candidates/rejected
PUT    /recruiter/decision
```

### Authentication Header
```http
Authorization: Bearer <JWT_TOKEN>
```

### Response Format
```json
{
  "success": true,
  "statusCode": 200,
  "message": "Success",
  "data":  "responseData" ,
  "errors": [],
  "path": "/api/v1/endpoint",
  "timestamp": "2026-05-05T10:30:00"
}
```

---

## Project Structure

```
src/main/java/com/hireflow/
├── HireflowBackendApplication.java     [Entry point]
├── auth/                               [Authentication]
│   ├── controller/
│   ├── service/
│   └── dto/
├── candidate/                          [Candidate management]
│   ├── controller/
│   ├── service/
│   └── dto/
├── interview/                          [Interview management]
│   ├── controller/
│   ├── service/
│   └── dto/
├── recruiter/                          [Recruiter dashboard]
│   ├── controller/
│   ├── service/
│   └── dto/
├── common/                             [Shared]
│   ├── entity/                 [5 main entities]
│   ├── repository/             [JPA repositories]
│   ├── enums/                  [Role, Status enums]
│   ├── exception/              [Error handling]
│   └── response/               [API response wrapper]
├── config/                             [Configuration]
│   ├── OpenAPIConfig.java
│   └── CorsConfig.java
└── security/                           [JWT & Security]
    ├── SecurityConfig.java
    ├── JwtUtil.java
    ├── JwtAuthenticationFilter.java
    └── CustomUserDetailsService.java
```

---

## Database

### Tables
- **users** - All user accounts (Candidate, Interviewer, Recruiter)
- **candidate_profile** - Candidate details (skills, experience, resume)
- **interview_slot** - Interview time slots created by interviewers
- **interview_booking** - Candidate interview bookings
- **feedback** - Interviewer feedback & ratings

### Key Enums
- **Role**: CANDIDATE, INTERVIEWER, RECRUITER
- **ApplicationStatus**: APPLIED, INTERVIEW_SCHEDULED, SELECTED, REJECTED
- **BookingStatus**: BOOKED, COMPLETED, CANCELLED
- **SlotStatus**: AVAILABLE, BOOKED
- **RecommendationStatus**: STRONG_HIRE, HIRE, REJECT

---

## Troubleshooting

### MySQL Connection Error
```
Error: java.sql.SQLException: Unable to connect to database
```
**Fix**: Verify MySQL is running, database exists, credentials in application.properties

### Port 8080 Already in Use
```bash
# macOS/Linux
lsof -i :8080
kill -9 <PID>

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Or change port
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### JWT Token Issues
- Verify token in Authorization header: `Authorization: Bearer <token>`
- Check token expiration (24 hours by default)
- Regenerate token by logging in again

### CORS Error
- Verify CorsConfig allows frontend URL
- Check `--spring.profiles.active=dev` for development

---

## Security Notes

 **Before Production**:
1. Change `jwt.secret` in application.properties to a strong unique value
2. Update database credentials
3. Set `spring.jpa.hibernate.ddl-auto=validate` (not update)
4. Disable SQL logging: `spring.jpa.show-sql=false`
5. Set logging level to INFO: `logging.level.com.hireflow=INFO`

---

## Configuration

### application.properties
```properties
# Server
server.port=8080
server.servlet.context-path=/api/v1

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/hireflow_db
spring.datasource.username=root
spring.datasource.password=Chaitanya@26

# JWT
jwt.secret=Q2hhaXRhbnlhS2FydGhpa0hpcmVGbG93U3VwZXJTZWNyZXRLZXlGb3JKV1QyMDI2
jwt.expiration=86400000

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Logging
logging.level.com.hireflow=DEBUG
```

---

## Technologies

---
| Component  | Technology            | Version     |
|------------|-----------------------|-------------|
| Framework  | Spring Boot           | 4.0.6       |
| Language   | Java                  | 17 LTS      |
| Database   | MySQL                 | 8.x         |
| Security   | Spring Security + JWT | JJWT 0.11.5 |
| Build      | Maven                 | 3.x         |
| ORM        | Hibernate JPA         | 6.2.x       |
| API Docs   | Swagger/OpenAPI       | 3.0.2       |
| Utils      | Lombok                | Latest      |

---

## Development

### Build
```bash
mvn clean install
```

### Test
```bash
mvn test
mvn test -Dtest=AuthServiceTest
```

### Debug
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--debug"
```
---

## Support

- **Swagger UI**: http://localhost:8080/api/v1/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/v1/api-docs

---
