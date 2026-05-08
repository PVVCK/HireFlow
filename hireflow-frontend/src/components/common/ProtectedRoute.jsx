import { Navigate } from "react-router-dom";

function ProtectedRoute({ children, allowedRole }) {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");

    if (!token) {
        return <Navigate to="/" replace />;
    }

    if (allowedRole && role !== allowedRole) {

        if (role === "CANDIDATE") {
            return <Navigate to="/candidate" replace />;
        }

        if (role === "INTERVIEWER") {
            return <Navigate to="/interviewer" replace />;
        }

        if (role === "RECRUITER") {
            return <Navigate to="/recruiter" replace />;
        }

        return <Navigate to="/" replace />;
    }

    return children;
}

export default ProtectedRoute;