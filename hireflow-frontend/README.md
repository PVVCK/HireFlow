#  HireFlow Frontend - React Application


## Overview

HireFlow Frontend is a React application that provides interfaces for three user roles:
- **Candidate**: Profile management, browse interview slots, book/reschedule interviews
- **Interviewer**: Create interview slots, view bookings, submit feedback
- **Recruiter**: Dashboard with analytics, manage candidate pipeline, make hiring decisions

**Backend API**: `http://localhost:8080/api/v1`

---

## Prerequisites

```
✓ Node.js 16+  (https://nodejs.org/)
✓ npm 8+       (comes with Node.js)
```

**Verify**:
```bash
node --version
npm --version
```

---

## Setup

### 1. Install Dependencies
```bash
cd hireflow-frontend
npm install
```

### 2. Configure API Endpoint
Check `src/api/axiosConfig.js`:
```javascript
const api = axios.create({
    baseURL: "http://localhost:8080/api/v1",
    headers: {
        "Content-Type": "application/json"
    }
});
```

Ensure backend is running on `http://localhost:8080`

---

## Running

### Development Server
```bash
npm run dev
```
Starts on `http://localhost:5173`

### Production Build
```bash
npm run build
npm run preview
```

### Linting
```bash
npm run lint
```

---

## Project Structure

```
src/
├── main.jsx                    [Entry point]
├── App.jsx                     [Main routing]
│
├── pages/                      [Page components]
│   ├── Register.jsx           [Login/Register]
│   ├── CandidateDashboard.jsx [Candidate interface (480 lines)]
│   ├── InterviewerDashboard.jsx [Interviewer interface (472 lines)]
│   └── RecruiterDashboard.jsx [Recruiter interface (332 lines)]
│
├── api/                        [API integration]
│   ├── axiosConfig.js         [Axios instance + interceptors]
│   ├── authApi.js             [Auth endpoints]
│   ├── candidateApi.js        [Candidate endpoints]
│   ├── interviewApi.js        [Interview endpoints]
│   └── recruiterApi.js        [Recruiter endpoints]
│
├── components/
│   ├── layout/
│   │   ├── Navbar.jsx         [Global navigation]
│   │   └── Sidebar.jsx
│   ├── common/
│   │   ├── DashboardCard.jsx  [Reusable metric card]
│   │   ├── LoadingSpinner.jsx
│   │   └── StatusBadge.jsx
│   ├── candidate/
│   ├── interviewer/
│   └── recruiter/
│
├── constants/                  [Empty - for constants]
├── hooks/                      [Empty - for custom hooks]
└── utils/                      [Empty - for utilities]

public/
├── favicon.svg
└── icons.svg

index.html                      [HTML entry]
vite.config.js                 [Vite configuration]
package.json                   [Dependencies]
```

---

## Key Features

### Pages (4 Routes)
| Page                  | Route          | Role         | Status |
|-----------------------|----------------|--------------|--------|
| Register              | `/`            | All          | Active |
| Candidate Dashboard   | `/candidate`   | CANDIDATE    | Active |
| Interviewer Dashboard | `/interviewer` | INTERVIEWER  | Active |
| Recruiter Dashboard   | `/recruiter`   | RECRUITER    | Active |

### Components
- **Navbar**: Global navigation with role-based links, login/logout
- **DashboardCard**: Reusable metric card with animations
- **LoadingSpinner**: Loading indicator
- **StatusBadge**: Status display component

### API Modules (5)
```
authApi.js         - Login/Register
candidateApi.js    - Candidate operations (6+ endpoints)
interviewApi.js    - Interview slot/feedback (6+ endpoints)
recruiterApi.js    - Dashboard & decisions (6+ endpoints)
axiosConfig.js     - JWT token handling + interceptors
```

---

## Technologies

| Component     | Technology     | Version |
|---------------|----------------|---------|
| Framework     | React          | 19.2.5  |
| Build Tool    | Vite           | 8.0.10  |
| Routing       | React Router   | 7.14.2  |
| HTTP Client   | Axios          | 1.15.2  |
| UI Framework  | Bootstrap      | 5.3.8   |
| Animations    | Framer Motion  | 12.38.0 |
| Icons         | Lucide React   | 1.11.0  |
| Notifications | React Toastify | 11.1.0  |

---

## Development

### Build
```bash
npm run build
```

### Lint
```bash
npm run lint
npm run lint -- --fix
```

### File Structure Rules
- One component per file
- Keep pages in `/pages`
- Reusable components in `/components`
- API calls in `/api` modules
- Use Bootstrap grid system

---

## Authentication

### Token Management
Handled automatically by `axiosConfig.js`:
- Reads token from localStorage
- Adds `Authorization: Bearer <token>` header to all requests
- Auto-redirects to login on 401 error

### Login Flow
1. User submits credentials → `/auth/register` or `/auth/login`
2. Backend returns JWT token
3. Frontend stores in localStorage
4. Subsequent requests include token
5. Role-based routing in App.jsx

---

## Troubleshooting

### Backend Connection Error
```
Error: Network Error / 503 Service Unavailable
```
**Fix**: 
- Verify backend is running: `http://localhost:8080/api/v1/swagger-ui.html`
- Check Axios baseURL in `src/api/axiosConfig.js`

### Port 5173 Already in Use
```bash
npm run dev -- --port 3000
```

### Build Fails
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
npm run build
```

### Styling Issues
- Bootstrap CSS loaded in `index.html`
- Check browser DevTools → Network tab
- Verify `bootstrap@5.3.8` in package.json

### Login Not Working
- Verify backend is running
- Check browser localStorage (F12 → Application → Storage)
- Check Console for errors
- Verify JWT token in Authorization header

---

## Configuration

### Vite Config
`vite.config.js` - React plugin enabled with HMR

### Environment Variables (Optional)
Can add `.env` file for environment-specific configs:
```
VITE_API_URL=http://localhost:8080/api/v1
```

Then use: `import.meta.env.VITE_API_URL`

---

## Performance Tips

### Code Splitting
- Routes are automatically code-split by Vite
- Each dashboard page loads on demand

### Optimization
- Framer Motion animations optimized
- Bootstrap minified in build
- Lucide icons tree-shaken

### Local Storage
```javascript
// Store JWT token
localStorage.setItem("token", response.data.data.token);
localStorage.setItem("role", response.data.data.role);

// Retrieve
const token = localStorage.getItem("token");

// Clear on logout
localStorage.clear();
```

---

## Common API Calls

### Authentication
```javascript
import authApi from "./api/authApi";

// Register
await authApi.register({ email, password, role, name });

// Login
const response = await authApi.login({ email, password });
localStorage.setItem("token", response.data.data.token);
```

### Candidate Operations
```javascript
import candidateApi from "./api/candidateApi";

await candidateApi.getAvailableSlots();
await candidateApi.bookSlot({ slotId, candidateId });
await candidateApi.rescheduleBooking({ bookingId, newSlotId });
```

### Recruiter Operations
```javascript
import recruiterApi from "./api/recruiterApi";

await recruiterApi.getDashboardStats();
await recruiterApi.getPendingCandidates();
await recruiterApi.updateHiringDecision({ candidateId, status });
```

---

## Styling Guide

### Bootstrap Classes
- Grid: `container`, `row`, `col-md-6`
- Buttons: `btn btn-primary`, `btn btn-success`
- Cards: `card p-4 shadow-lg`
- Alerts: `alert alert-success`, `alert alert-danger`


### Animations
Using Framer Motion:
```javascript
<motion.div
    whileHover={{ scale: 1.05 }}
    initial={{ opacity: 0 }}
    animate={{ opacity: 1 }}
>
```

---

## Support

- **Backend API**: http://localhost:8080/api/v1/swagger-ui.html
- **Vite Docs**: https://vitejs.dev/
- **React Router**: https://reactrouter.com/
- **Bootstrap**: https://getbootstrap.com/
- **Axios**: https://axios-http.com/

---
