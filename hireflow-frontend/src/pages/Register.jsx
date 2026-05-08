import { useNavigate, useLocation } from "react-router-dom";
import { registerUser, loginUser } from "../api/authApi";
import { toast } from "react-toastify";
import { useState } from "react";

function Register() {

    const navigate = useNavigate();
    const location = useLocation();
    const isLogin = location.pathname === "/login";


    const [authError, setAuthError] = useState("");
    const [loading, setLoading] = useState(false);



    const [formData, setFormData] = useState({
        name: "",
        email: "",
        password: "",
        role: "CANDIDATE"
    });

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        setAuthError("");
        setLoading(true);

        try {

            let response;

            if (isLogin) {
                response = await loginUser({
                    email: formData.email.trim(),
                    password: formData.password.trim()
                });

                // Store JWT token first
                localStorage.setItem(
                    "token",
                    response.data.data.token
                );

                // Store user info
                localStorage.setItem(
                    "userId",
                    response.data.data.userId
                );

                localStorage.setItem(
                    "role",
                    response.data.data.role
                );

                localStorage.setItem(
                    "username",
                    response.data.data.name
                );

                toast.success("Login successful");

                if (response.data.data.role === "CANDIDATE") {
                    navigate("/candidate");
                }
                else if (response.data.data.role === "INTERVIEWER") {
                    navigate("/interviewer");
                }
                else if (response.data.data.role === "RECRUITER") {
                    navigate("/recruiter");
                }

            } else {

                response = await registerUser(formData);

                toast.success(
                    "Registration successful. Please login."
                );

                navigate("/login");

                setFormData({
                    name: "",
                    email: "",
                    password: "",
                    role: "CANDIDATE"
                });
            }

        } catch (error) {
            console.error(error);

            if (isLogin) {
                setAuthError(
                    "Invalid email or password"
                );
            } else {
                setAuthError(
                    error?.response?.data?.message ||
                    "Registration failed"
                );
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <div
            className="container d-flex justify-content-center align-items-center"
            style={{ minHeight: "100vh" }}
        >
            <div className="card p-5 shadow-lg w-50">
                <h2 className="text-center mb-4">
                    {isLogin ? "Login" : "Register"}
                </h2>

                <form onSubmit={handleSubmit}>

                    {!isLogin && (
                        <>
                            <input
                                type="text"
                                className="form-control mb-3"
                                placeholder="Full Name"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                />

                            <select
                                className="form-select mb-3"
                                name="role"
                                value={formData.role}
                                onChange={handleChange}
                            >
                                <option value="CANDIDATE">
                                    Candidate
                                </option>

                                <option value="INTERVIEWER">
                                    Interviewer
                                </option>

                                <option value="RECRUITER">
                                    Recruiter
                                </option>
                            </select>
                        </>
                    )}

                    <input
                        type="email"
                        className="form-control mb-3"
                        placeholder="Email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                    />

                    <input
                        type="password"
                        className="form-control mb-3"
                        placeholder="Password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                    />

                    {authError && (
                        <div className="text-danger mb-3 fw-semibold text-center">
                            {authError}
                        </div>
                    )}

                    <button
                        className="btn btn-dark w-100"
                        disabled={loading}
                    >
                        {loading
                            ? "Please wait..."
                            : isLogin
                                ? "Login"
                                : "Register"}
                    </button>
                </form>

                <button
                    className="btn btn-link mt-3"
                    onClick={() => {
                        setAuthError("");

                        if (isLogin) {
                            navigate("/register");
                        } else {
                            navigate("/login");
                        }
                    }}
                >
                    {isLogin
                        ? "New user? Register here"
                        : "Already have an account? Login"}
                </button>
            </div>
        </div>
    );
}

export default Register;