import { Link, useNavigate } from "react-router-dom";
import { Briefcase } from "lucide-react";

function Navbar() {
    const navigate = useNavigate();

    const role = localStorage.getItem("role");
    const username = localStorage.getItem("username");

    const handleLogout = () => {
        localStorage.clear();
        navigate("/login");
    };

    const getDashboardRoute = () => {
        if (role === "CANDIDATE") {
            return "/candidate";
        }

        if (role === "INTERVIEWER") {
            return "/interviewer";
        }

        if (role === "RECRUITER") {
            return "/recruiter";
        }

        return "/";
    };

    return (
        <nav
            className="navbar navbar-expand-lg px-4 py-3"
            style={{
                background:
                    "linear-gradient(90deg,#0f172a,#1e293b,#111827)"
            }}
        >
            {/* Brand */}
            <button
                className="navbar-brand text-white fw-bold d-flex align-items-center gap-2 border-0 bg-transparent"
                onClick={() => navigate(getDashboardRoute())}
            >
                <Briefcase size={24} />
                HireFlow
            </button>

            <div className="ms-auto d-flex align-items-center gap-3">

                {/* Dashboard Button */}
                {role && (
                    <Link
                        className="btn btn-outline-light"
                        to={getDashboardRoute()}
                    >
                        My Dashboard
                    </Link>
                )}

                {/* Username */}
                {username && (
                    <span className="text-white fw-semibold">
                        Welcome, {username}
                    </span>
                )}

                {/* Login Button */}
                {!role && (
                    <Link
                        className="btn btn-light"
                        to="/"
                    >
                        Login
                    </Link>
                )}

                {/* Logout Button */}
                {role && (
                    <button
                        className="btn btn-danger"
                        onClick={handleLogout}
                    >
                        Logout
                    </button>
                )}
            </div>
        </nav>
    );
}

export default Navbar;