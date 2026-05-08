import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Register from "./pages/Register";
import CandidateDashboard from "./pages/CandidateDashboard";
import InterviewerDashboard from "./pages/InterviewerDashboard";
import RecruiterDashboard from "./pages/RecruiterDashboard";
import Navbar from "./components/layout/Navbar";
import ProtectedRoute from "./components/common/ProtectedRoute";
import PublicRoute from "./components/common/PublicRoute";

function App() {
    return (
        <BrowserRouter>
            {/* Navbar visible on all pages */}
            <Navbar />

            <Routes>

                {/* Redirect root to login */}
                <Route
                    path="/"
                    element={<Navigate to="/login" replace />}
                />

                {/* Login route */}
                <Route
                    path="/login"
                    element={
                        <PublicRoute>
                            <Register isLoginDefault={true} />
                        </PublicRoute>
                    }
                />

                {/* Register route */}
                <Route
                    path="/register"
                    element={
                        <PublicRoute>
                            <Register isLoginDefault={false} />
                        </PublicRoute>
                    }
                />

                {/* Candidate route */}
                <Route
                    path="/candidate"
                    element={
                        <ProtectedRoute allowedRole="CANDIDATE">
                            <CandidateDashboard />
                        </ProtectedRoute>
                    }
                />

                {/* Interviewer route */}
                <Route
                    path="/interviewer"
                    element={
                        <ProtectedRoute allowedRole="INTERVIEWER">
                            <InterviewerDashboard />
                        </ProtectedRoute>
                    }
                />

                {/* Recruiter route */}
                <Route
                    path="/recruiter"
                    element={
                        <ProtectedRoute allowedRole="RECRUITER">
                            <RecruiterDashboard />
                        </ProtectedRoute>
                    }
                />
            </Routes>
        </BrowserRouter>
    );
}

export default App;