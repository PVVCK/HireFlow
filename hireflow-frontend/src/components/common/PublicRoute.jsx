import { Navigate } from "react-router-dom";

function PublicRoute({ children }) {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");

    if (token && role === "CANDIDATE") {
        return <Navigate to="/candidate" replace />;
    }

    if (token && role === "INTERVIEWER") {
        return <Navigate to="/interviewer" replace />;
    }

    if (token && role === "RECRUITER") {
        return <Navigate to="/recruiter" replace />;
    }

    return children;
}

export default PublicRoute;