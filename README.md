# HireFlow - Complete Recruitment Management Platform

---

## Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [System Architecture](#-system-architecture)
- [Prerequisites](#-prerequisites)
- [Quick Start](#-quick-start)
- [Backend Setup](#-backend-setup)
- [Frontend Setup](#-frontend-setup)
- [Project Structure](#-project-structure)
- [Database Schema](#-database-schema)
- [API Documentation](#-api-documentation)
- [Running Together](#-running-both-services)
- [Configuration](#-configuration)
- [Troubleshooting](#-troubleshooting)

---

## Overview

**HireFlow** is a comprehensive recruitment management platform that streamlines the entire hiring workflow. It provides three distinct interfaces for three user roles:

- **Candidates**: Profile management, discover interview slots, book and reschedule interviews
- **Interviewers**: Create interview slots, schedule interviews, submit feedback and recommendations
- **Recruiters**: Dashboard with analytics, manage candidate pipeline, make final hiring decisions

### Tech Stack

| Layer           | Technologies                                      |
|-----------------|---------------------------------------------------|
| **Frontend**    | React 19 + Vite 8 + Bootstrap 5 + Framer Motion   |
| **Backend**     | Spring Boot 4.0.6 + Spring Security + JWT         |
| **Database**    | MySQL 8.x with Hibernate JPA                      |
| **Build Tools** | Maven (Backend) + npm (Frontend)                  |

---

##  Features

### Authentication & Security
 JWT-based authentication  
 Role-based access control (CANDIDATE, INTERVIEWER, RECRUITER)  
 BCrypt password encryption  
 CORS support  
 Input validation  

### Candidate Features
 Profile management (skills, experience, resume)  
 Browse available interview slots  
 Book interview slots  
 Reschedule or cancel bookings  
 Track application status  

### Interviewer Features
 Create & manage interview slots  
 View scheduled interviews  
 Submit feedback (rating, comments, recommendation)  
 Track candidate evaluations  

### Recruiter Features
 Dashboard with statistics  
 Pipeline view (All, Pending, Selected, Rejected)  
 Make hiring decisions  
 Export candidate data  
 Track hiring metrics  

---

## ️ System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    FRONTEND (React + Vite)                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐           │
│  │  Candidate   │  │ Interviewer  │  │  Recruiter   │           │
│  │  Dashboard   │  │  Dashboard   │  │  Dashboard   │           │
│  └──────────────┘  └──────────────┘  └──────────────┘           │
│         │                 │                 │                   │
│         └─────────────────┴─────────────────┘                   │
│                      │ HTTP/REST                                │
│                      ↓                                          │
├─────────────────────────────────────────────────────────────────┤
│            BACKEND API (Spring Boot 4.0.6)                      │
│  ┌─────────────┐  ┌─────────────┐  ┌──────────────┐             │
│  │    Auth     │  │  Candidate  │  │  Interview   │             │
│  │  Endpoints  │  │  Endpoints  │  │  Endpoints   │             │
│  └─────────────┘  └─────────────┘  └──────────────┘             │
│                                                                 │
│  ┌──────────────┐  ┌──────────────────────────────┐             │
│  │  Recruiter   │  │  Security + JWT + Exception  │             │
│  │  Endpoints   │  │  Handling                    │             │ 
│  └──────────────┘  └──────────────────────────────┘             │
│         │                                         |             │
│         └──────────────────┬──────────────────────┘             │
│                            │ JPA + Hibernate                    │
│                            ↓                                    │
├─────────────────────────────────────────────────────────────────┤
│                    DATABASE (MySQL 8.x)                         │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  users | candidate_profile | interview_slot |            │   │
│  │  interview_booking | feedback                            │   │
│  └──────────────────────────────────────────────────────────┘   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Prerequisites

### System Requirements
- **RAM**: 4GB minimum (8GB recommended)
- **Disk**: 2GB free space
- **OS**: Windows / macOS / Linux

### Software Requirements

```bash
# Java 17+
java -version

# Maven 3.6+
mvn -version

# Node.js 16+
node --version
npm --version

# MySQL 8.0+
mysql --version
```

---

## Quick Start

### Option 1: Run Everything (Recommended)
```bash
# Terminal 1 - Start MySQL
# (Ensure MySQL is running on localhost:3306)

# Terminal 2 - Start Backend
cd hireflow-backend/hireflow-backend
mvn clean install
mvn spring-boot:run
# Backend runs on http://localhost:8080/api/v1

# Terminal 3 - Start Frontend
cd hireflow-frontend
npm install
npm run dev
# Frontend runs on http://localhost:5173
```

### Option 2: Docker (Future Enhancement)
```bash
docker-compose up -d
```

---

## 🔧 Backend Setup

### 1. Database Setup
```bash
mysql -u root -p

# In MySQL:
CREATE DATABASE hireflow_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EXIT;
```

### 2. Configure Backend
Edit `hireflow-backend/hireflow-backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hireflow_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

jwt.secret=Q2hhaXRhbnlhS2FydGhha0hpcmVGbG93U3VwZXJTZWNyZXRLZXlGb3JKV1QyMDI2
jwt.expiration=86400000  # 24 hours
```

### 3. Build & Run
```bash
cd hireflow-backend/hireflow-backend

# Install dependencies
mvn clean install

# Run
mvn spring-boot:run

# Verify: Open http://localhost:8080/api/v1/swagger-ui.html
```

### Backend Endpoints
```
Base URL: http://localhost:8080/api/v1

Authentication:
  POST /auth/register
  POST /auth/login

Candidate (role: CANDIDATE):
  POST   /candidates/{userId}/profile
  GET    /candidates/slots
  POST   /candidates/bookings
  PUT    /candidates/bookings/reschedule
  DELETE /candidates/bookings/{bookingId}

Interview (role: INTERVIEWER):
  POST /interviews/slots
  PUT  /interviews/slots/{slotId}
  POST /interviews/feedback

Recruiter (role: RECRUITER):
  GET /recruiter/dashboard
  GET /recruiter/candidates/pending
  PUT /recruiter/decision
```

---

## Frontend Setup

### 1. Install Dependencies
```bash
cd hireflow-frontend
npm install
```

### 2. Configure API
Check `src/api/axiosConfig.js`:
```javascript
const api = axios.create({
    baseURL: "http://localhost:8080/api/v1"
});
```

Ensure backend is running on `http://localhost:8080`

### 3. Run Development Server
```bash
npm run dev
# Frontend runs on http://localhost:5173
```

### Frontend Pages
```
/               - Register/Login
/candidate      - Candidate Dashboard
/interviewer    - Interviewer Dashboard
/recruiter      - Recruiter Dashboard
```

---

## Project Structure

```
HireFlow/
│
├── hireflow-backend/
│   └── hireflow-backend/
│       ├── src/main/java/com/hireflow/
│       │   ├── auth/              [Registration & login]
│       │   ├── candidate/         [Candidate operations]
│       │   ├── interview/         [Slot & feedback]
│       │   ├── recruiter/         [Dashboard & decisions]
│       │   ├── common/            [Entities, repos, exceptions]
│       │   ├── config/            [OpenAPI, CORS]
│       │   └── security/          [JWT, authentication]
│       ├── pom.xml
│       └── README.md
│
├── hireflow-frontend/
│   ├── src/
│   │   ├── pages/                 [4 page components]
│   │   ├── api/                   [API integration]
│   │   ├── components/            [Reusable components]
│   │   └── App.jsx                [Main routing]
│   ├── package.json
│   ├── vite.config.js
│   └── README.md
│
└── README.md                       [This file]
```

---

## Database Schema

### Entity Relationship Diagram

```
USERS ─────────────┬─────────────┬──────────────┐
                   │             │              │
         (1:1)     │     (1:N)   │              │
                   ▼             ▼              ▼
         CANDIDATE_PROFILE  INTERVIEW_SLOT  BOOKING
                   │                           │
                   │               (1:N)       │
                   │                           │
                   └───────────┬───────────────┘
                               │
                               ▼
                           FEEDBACK
```

### Tables

#### users
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(150) UNIQUE,
    password VARCHAR(255),
    role ENUM('CANDIDATE', 'INTERVIEWER', 'RECRUITER'),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

#### candidate_profile
```sql
CREATE TABLE candidate_profile (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE,
    skills VARCHAR(500),
    experience_years INT,
    resume_url VARCHAR(255),
    application_status ENUM('APPLIED', 'INTERVIEW_SCHEDULED', 'SELECTED', 'REJECTED'),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

#### interview_slot
```sql
CREATE TABLE interview_slot (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    interviewer_id BIGINT,
    interview_date DATE,
    start_time TIME,
    end_time TIME,
    slot_status ENUM('AVAILABLE', 'BOOKED'),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (interviewer_id) REFERENCES users(id)
);
```

#### interview_booking
```sql
CREATE TABLE interview_booking (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    slot_id BIGINT,
    candidate_id BIGINT,
    booking_status ENUM('BOOKED', 'COMPLETED', 'CANCELLED'),
    scheduled_at DATETIME,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (slot_id) REFERENCES interview_slot(id),
    FOREIGN KEY (candidate_id) REFERENCES users(id)
);
```

#### feedback
```sql
CREATE TABLE feedback (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT UNIQUE,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    comments VARCHAR(1000),
    recommendation_status ENUM('STRONG_HIRE', 'HIRE', 'REJECT'),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES interview_booking(id)
);
```

---

##  API Documentation

### Interactive Swagger UI
```
http://localhost:8080/api/v1/swagger-ui.html
```

### Authentication Flow
```
1. User registers/logs in with email & password
2. Backend hashes password with BCrypt
3. Server validates & generates JWT token
4. Client stores token in localStorage
5. Subsequent requests include: Authorization: Bearer <token>
6. Server validates token using JwtAuthenticationFilter
7. Role-based authorization applied
```

### Response Format (All Endpoints)
```json
{
  "success": true,
  "statusCode": 200,
  "message": "Operation successful",
  "data": { /* response data */ },
  "errors": [],
  "path": "/api/v1/endpoint",
  "timestamp": "2026-05-08T10:30:00"
}
```

### HTTP Status Codes
| Code  | Meaning                        |
|-------|--------------------------------|
| 200   | OK                             |
| 201   | Created                        |
| 400   | Bad Request                    |
| 401   | Unauthorized                   |
| 403   | Forbidden (insufficient role)  |
| 404   | Not Found                      |
| 409   | Conflict                       |
| 500   | Server Error                   |

---

## Running Both Services

### Terminal Setup (Recommended)

**Terminal 1 - MySQL** (if not running as service)
```bash
# macOS
mysql.server start

# Windows
# Start MySQL from Services or run: mysqld
```

**Terminal 2 - Backend**
```bash
cd hireflow-backend/hireflow-backend
mvn spring-boot:run
# Runs on http://localhost:8080/api/v1
```

**Terminal 3 - Frontend**
```bash
cd hireflow-frontend
npm run dev
# Runs on http://localhost:5173
```

### Verify All Running
```bash
# Check Backend
curl http://localhost:8080/api/v1/swagger-ui.html

# Check Frontend
# Open http://localhost:5173 in browser
```

---

##  Configuration

### Backend Configuration
`hireflow-backend/hireflow-backend/src/main/resources/application.properties`

```properties
# Server
server.port=8080
server.servlet.context-path=/api/v1

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/hireflow_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

# JWT (Change in production!)
jwt.secret=Q2hhaXRhbnlhS2FydGhha0hpcmVGbG93U3VwZXJTZWNyZXRLZXlGb3JKV1QyMDI2
jwt.expiration=86400000

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# Logging
logging.level.com.hireflow=INFO
```

### Frontend Configuration
`hireflow-frontend/src/api/axiosConfig.js`

```javascript
const api = axios.create({
    baseURL: "http://localhost:8080/api/v1",
    headers: { "Content-Type": "application/json" }
});
```

---

## Troubleshooting

### Backend Issues

**MySQL Connection Error**
```
Error: java.sql.SQLException: Unable to connect to database
```
```bash
# Fix: Verify MySQL is running
mysql -u root -p
# Then check database exists: SHOW DATABASES;
```

**Port 8080 Already in Use**
```bash
# macOS/Linux
lsof -i :8080 | grep LISTEN
kill -9 <PID>

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

**Build Fails with Maven**
```bash
# Clear cache and rebuild
mvn clean install -U
```

### Frontend Issues

**API Connection Failed**
```bash
# Verify backend is running
curl http://localhost:8080/api/v1/swagger-ui.html

# Check axiosConfig.js baseURL
# Check browser Console for errors
```

**Port 5173 Already in Use**
```bash
npm run dev -- --port 3000
```

**Styling Issues**
- Check Bootstrap CSS is loaded: F12 → Network
- Verify Bootstrap in package.json
- Clear browser cache: Ctrl+Shift+Delete

**Login Errors**
- Check localStorage: F12 → Application → Storage
- Verify JWT token in Console
- Check backend logs for authentication errors

### Common Errors

**CORS Error**
```
Access to XMLHttpRequest blocked by CORS policy
```
- Verify backend CORS is configured
- Check CorsConfig.java allows frontend URL

**Token Expired**
- JWT expires in 24 hours (configurable)
- Log in again to get new token

---

## Technology Stack Summary

### Backend
- **Spring Boot 4.0.6** - Framework
- **Java 17** - Language
- **Spring Security** - Authentication
- **JJWT 0.11.5** - JWT tokens
- **Hibernate** - ORM
- **MySQL** - Database
- **Maven** - Build
- **Lombok** - Code generation

### Frontend
- **React 19** - UI framework
- **Vite 8** - Build tool
- **Bootstrap 5** - Styling
- **Axios** - HTTP client
- **Framer Motion** - Animations
- **Lucide React** - Icons
- **React Router** - Routing
- **React Toastify** - Notifications

---

##  Security Notes

 **Before Production**:
1. Change `jwt.secret` to a strong, unique value
2. Update database credentials
3. Enable HTTPS
4. Set `spring.jpa.hibernate.ddl-auto=validate`
5. Disable SQL logging in production
6. Change logging levels to INFO

---

##  Support

### Documentation
- **Backend Docs**: `hireflow-backend/hireflow-backend/README.md`
- **Frontend Docs**: `hireflow-frontend/README.md`
- **Swagger UI**: http://localhost:8080/api/v1/swagger-ui.html

### Debugging
- Check browser Console for frontend errors
- Check backend logs for API errors
- Use Swagger UI to test endpoints
- Check MySQL with: `mysql -u root -p`

---

##  Quick Commands

```bash
# Backend
cd hireflow-backend/hireflow-backend
mvn clean install        # Install dependencies
mvn spring-boot:run      # Run backend
mvn test                 # Run tests
mvn clean package        # Production build

# Frontend
cd hireflow-frontend
npm install              # Install dependencies
npm run dev              # Run development server
npm run build            # Production build
npm run preview          # Preview build
npm run lint             # Check code style
```

---

##  Testing the Application

### 1. Register a User
- Navigate to http://localhost:5173
- Click Register
- Enter email, password, role (CANDIDATE/INTERVIEWER/RECRUITER), name

### 2. Login
- Use registered credentials
- Redirected to role-specific dashboard

### 3. Test Features (by role)

**As Candidate**:
- View available interview slots
- Book an interview
- Reschedule or cancel booking

**As Interviewer**:
- Create interview slot
- View bookings
- Submit feedback

**As Recruiter**:
- View dashboard statistics
- See pending candidates
- Make hiring decisions

---
