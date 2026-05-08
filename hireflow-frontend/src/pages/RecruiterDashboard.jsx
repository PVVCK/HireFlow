import { useEffect, useState } from "react";
import {
    getDashboardStats,
    getPendingCandidates,
    getSelectedCandidates,
    getRejectedCandidates,
    updateHiringDecision
} from "../api/recruiterApi";

import { motion } from "framer-motion";
import {
    Users,
    CheckCircle,
    XCircle,
    Briefcase
} from "lucide-react";

import { toast } from "react-toastify";


function RecruiterDashboard() {

    const [stats, setStats] = useState({});
    const [pendingCandidates, setPendingCandidates] = useState([]);
    const [selectedCandidates, setSelectedCandidates] = useState([]);
    const [rejectedCandidates, setRejectedCandidates] = useState([]);
    const [loading, setLoading] = useState(true);
    const [activeFilter, setActiveFilter] = useState("PENDING");



    const fetchDashboard = async () => {
        try {
            const statsRes = await getDashboardStats();
            const pendingRes = await getPendingCandidates();
            const selectedRes = await getSelectedCandidates();
            const rejectedRes = await getRejectedCandidates();

            setStats(statsRes.data.data);
            setPendingCandidates(pendingRes.data.data);
            setSelectedCandidates(selectedRes.data.data);
            setRejectedCandidates(rejectedRes.data.data);

        } catch (error) {
            console.error(error);
        }
    };

    const handleDecision = async (
        candidateId,
        status
    ) => {
        try {
            await updateHiringDecision({
                candidateId,
                applicationStatus: status
            });

            toast.success(`Candidate ${status} successfully`);

            fetchDashboard();

        } catch (error) {
            console.error(error);
            toast.error("Decision update failed");
        }
    };

    useEffect(() => {
        const loadDashboard = async () => {
            try {
                setLoading(true);
                await fetchDashboard();
            } finally {
                setLoading(false);
            }
        };

        loadDashboard();
    }, []);

    if (loading) {
        return (
            <div
                className="d-flex justify-content-center align-items-center"
                style={{
                    height: "100vh",
                    background: "#0f172a",
                    color: "white"
                }}
            >
                <div className="text-center">
                    <div
                        className="spinner-border text-light mb-3"
                        role="status"
                    ></div>

                    <h4>Loading Recruitment Dashboard...</h4>
                </div>
            </div>
        );
    }

    const getFilteredCandidates = () => {
        if (activeFilter === "PENDING") return pendingCandidates;
        if (activeFilter === "SELECTED") return selectedCandidates;
        if (activeFilter === "REJECTED") return rejectedCandidates;
        return [];
    };

    const filteredCandidates = getFilteredCandidates();

    return (
        <div
            className="container-fluid p-4"
            style={{
                minHeight: "100vh",
                background:
                    "linear-gradient(135deg,#0f172a,#1e293b,#111827)"
            }}
        >
            {/* Hero */}
            <motion.div
                className="text-center text-white mb-5"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
            >
                <h1 className="display-4 fw-bold">
                    Recruitment Command Center
                </h1>

                <p className="lead">
                    Manage hiring pipeline and final decisions
                </p>
            </motion.div>

            <div className="d-flex justify-content-center gap-3 mb-4">
                {["PENDING", "SELECTED", "REJECTED"].map((status) => (
                    <button
                        key={status}
                        className={`btn ${
                            activeFilter === status
                                ? "btn-light"
                                : "btn-outline-light"
                        }`}
                        onClick={() => setActiveFilter(status)}
                    >
                        {status}
                    </button>
                ))}
            </div>

            {/* Analytics Cards */}
            <div className="row mb-5">

                <div className="col-md-3">
                    <motion.div
                        className="card p-4 text-center shadow-lg"
                        whileHover={{ scale: 1.05 }}
                    >
                        <Users size={40} />
                        <h5>Total Candidates</h5>
                        <h2>{stats.totalCandidates || 0}</h2>
                    </motion.div>
                </div>

                <div className="col-md-3">
                    <motion.div
                        className="card p-4 text-center shadow-lg"
                        whileHover={{ scale: 1.05 }}
                    >
                        <Briefcase size={40} />
                        <h5>Scheduled Interviews</h5>
                        <h2>{stats.totalScheduledInterviews || 0}</h2>
                    </motion.div>
                </div>

                <div className="col-md-3">
                    <motion.div
                        className="card p-4 text-center shadow-lg"
                        whileHover={{ scale: 1.05 }}
                    >
                        <CheckCircle size={40} />
                        <h5>Selected</h5>
                        <h2>{stats.selectedCandidates || 0}</h2>
                    </motion.div>
                </div>

                <div className="col-md-3">
                    <motion.div
                        className="card p-4 text-center shadow-lg"
                        whileHover={{ scale: 1.05 }}
                    >
                        <XCircle size={40} />
                        <h5>Rejected</h5>
                        <h2>{stats.rejectedCandidates || 0}</h2>
                    </motion.div>
                </div>
            </div>

            <div className="text-white mb-4">
                <h2>{activeFilter.charAt(0) + activeFilter.slice(1).toLowerCase()} Candidates</h2>
            </div>

            <div className="row mb-5">
                {filteredCandidates.length === 0 ? (
                    <div className="col-12">
                        <div className="alert alert-light text-center">
                            No candidates found
                        </div>
                    </div>
                ) : (
                    filteredCandidates.map((candidate) => (
                        <div
                            className="col-md-6 mb-4"
                            key={candidate.candidateId}
                        >
                            <motion.div
                                className="card p-4 shadow-lg"
                                whileHover={{ scale: 1.03 }}
                            >
                                <h5>{candidate.candidateName}</h5>

                                <p>{candidate.email}</p>

                                <p>
                                    Skills: {candidate.skills || "N/A"}
                                </p>

                                <p>
                                    Experience: {candidate.experienceYears || 0} years
                                </p>

                                <p>
                                    Status: {candidate.applicationStatus}
                                </p>

                                {activeFilter === "PENDING" && (
                                    <div className="d-flex gap-2">
                                        <button
                                            className="btn btn-success w-50"
                                            onClick={() => {
                                                if (window.confirm("Select candidate?")) {
                                                    handleDecision(candidate.candidateId, "SELECTED");
                                                }
                                            }}
                                        >
                                            Select
                                        </button>

                                        <button
                                            className="btn btn-danger w-50"
                                            onClick={() => {
                                                if (window.confirm("Reject candidate?")) {
                                                    handleDecision(candidate.candidateId, "REJECTED");
                                                }
                                            }}
                                        >
                                            Reject
                                        </button>
                                    </div>
                                )}
                            </motion.div>
                        </div>
                    ))
                )}
            </div>


        </div>
    );
}

export default RecruiterDashboard;