import { useEffect, useState } from "react";
import {
    createSlot,
    updateSlot,
    deleteSlot,
    getBookings,
    getInterviewerSlots,
    submitFeedback
} from "../api/interviewApi";

import { motion } from "framer-motion";
import {
    Calendar,
    Users,
    MessageSquare
} from "lucide-react";
import { toast } from "react-toastify";

function InterviewerDashboard() {

    const interviewerId = Number(localStorage.getItem("userId"));

    const [slotData, setSlotData] = useState({
        interviewerId: interviewerId,
        interviewDate: "",
        startTime: "",
        endTime: ""
    });

    const [bookings, setBookings] = useState([]);
    const [slots, setSlots] = useState([]);
    const [editingSlotId, setEditingSlotId] = useState(null);
    const [loading, setLoading] = useState(true);

    const [feedbackData, setFeedbackData] = useState({});



    const fetchBookings = async () => {
        try {
            const response = await getBookings(interviewerId);
            setBookings(response.data.data);
        } catch (error) {
            console.error(error);
        }
    };

    const handleSlotChange = (e) => {
        setSlotData({
            ...slotData,
            [e.target.name]: e.target.value
        });
    };

    const fetchSlots = async () => {
        try {
            const response = await getInterviewerSlots(interviewerId);
            setSlots(response.data.data);
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        const loadDashboardData = async () => {
            try {
                setLoading(true);

                await fetchBookings();
                await fetchSlots();

            } finally {
                setLoading(false);
            }
        };

        loadDashboardData();
    }, []);

    const handleCreateSlot = async (e) => {
        e.preventDefault();

        try {
            if (editingSlotId) {
                await updateSlot(editingSlotId, {
                    interviewDate: slotData.interviewDate,
                    startTime: slotData.startTime,
                    endTime: slotData.endTime
                });

                toast.success("Slot updated successfully");
            } else {
                await createSlot(slotData);

                toast.success("Slot created successfully");
            }

            fetchSlots();

            setSlotData({
                interviewerId: interviewerId,
                interviewDate: "",
                startTime: "",
                endTime: ""
            });

            setEditingSlotId(null);

        } catch (error) {
            console.error(error);
            toast.error("Operation failed");
        }
    };

    const handleDeleteSlot = async (slotId) => {
        try {
            await deleteSlot(slotId);

            toast.success("Slot deleted successfully");

            fetchSlots();

        } catch (error) {
            console.error(error);
            toast.error("Slot deletion failed");
        }
    };

    const handleFeedbackChange = (bookingId, field, value) => {
        setFeedbackData(prev => ({
            ...prev,
            [bookingId]: {
                ...prev[bookingId],
                [field]: value
            }
        }));
    };

    const handleSubmitFeedback = async (bookingId) => {
        try {
            const data = feedbackData[bookingId];

            if (!data?.rating || !data?.recommendationStatus) {
                toast.error("Please fill all required fields");
                return;
            }

            await submitFeedback({
                bookingId: bookingId,
                rating: Number(data.rating),
                comments: data.comments || "",
                recommendationStatus: data.recommendationStatus
            });

            toast.success("Feedback submitted successfully");

            // clear only that booking's form
            setFeedbackData(prev => {
                const copy = { ...prev };
                delete copy[bookingId];
                return copy;
            });

            fetchBookings();

        } catch (error) {
            console.error(error);
            toast.error("Feedback submission failed");
        }
    };

    if (loading) {
        return (
            <div
                className="d-flex justify-content-center align-items-center"
                style={{
                    height: "100vh",
                    background: "#111827",
                    color: "white"
                }}
            >
                <div className="text-center">
                    <div
                        className="spinner-border text-light mb-3"
                        role="status"
                    ></div>

                    <h4>Loading Interview Dashboard...</h4>
                </div>
            </div>
        );
    }

    return (
        <div
            className="container-fluid p-4"
            style={{
                minHeight: "100vh",
                background:
                    "linear-gradient(135deg,#111827,#1e293b,#0f172a)"
            }}
        >
            {/* Hero */}
            <motion.div
                className="text-center text-white mb-5"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
            >
                <h1 className="display-4 fw-bold">
                    Interviewer Command Center
                </h1>

                <p className="lead">
                    Manage slots, interviews and candidate feedback
                </p>
            </motion.div>

            {/* Metrics */}
            <div className="row mb-5">
                <div className="col-md-4">
                    <motion.div
                        className="card p-4 text-center shadow-lg"
                        whileHover={{ scale: 1.05 }}
                    >
                        <Calendar size={40} />
                        <h5>Total Slots</h5>
                        <h2>{slots.length}</h2>
                    </motion.div>
                </div>

                <div className="col-md-4">
                    <motion.div
                        className="card p-4 text-center shadow-lg"
                        whileHover={{ scale: 1.05 }}
                    >
                        <Users size={40} />
                        <h5>Scheduled Bookings</h5>
                        <h2>{bookings.length}</h2>
                    </motion.div>
                </div>

                <div className="col-md-4">
                    <motion.div
                        className="card p-4 text-center shadow-lg"
                        whileHover={{ scale: 1.05 }}
                    >
                        <MessageSquare size={40} />
                        <h5>Feedback Tasks</h5>
                        <h2>
                            {
                                bookings.filter(
                                    booking => booking.bookingStatus === "BOOKED"
                                ).length
                            }
                        </h2>
                    </motion.div>
                </div>
            </div>

            {/* Create Slot */}
            <div className="row justify-content-center mb-5">
                <div className="col-md-8">
                    <motion.div
                        className="card p-5 shadow-lg"
                        whileHover={{ scale: 1.02 }}
                    >
                        <h3 className="text-center mb-4">
                            {editingSlotId
                                ? "Update Interview Slot"
                                : "Create Interview Slot"}
                        </h3>

                        <form onSubmit={handleCreateSlot}>
                            <input
                                type="date"
                                value={slotData.interviewDate}
                                name="interviewDate"
                                className="form-control mb-3"
                                onChange={handleSlotChange}
                            />

                            <input
                                type="time"
                                value={slotData.startTime}
                                name="startTime"
                                className="form-control mb-3"
                                onChange={handleSlotChange}
                            />

                            <input
                                type="time"
                                value={slotData.endTime}
                                name="endTime"
                                className="form-control mb-3"
                                onChange={handleSlotChange}
                            />
                            <button className="btn btn-dark w-100">
                                {editingSlotId
                                    ? "Update Slot"
                                    : "Create Slot"}
                            </button>
                        </form>
                    </motion.div>
                </div>
            </div>

            {/* My Slots Section */}
            <div className="text-white mb-4">
                <h2>My Interview Slots</h2>
            </div>

            <div className="row mb-5">
                {slots.length === 0 ? (
                    <div className="col-12">
                        <div className="alert alert-light text-center">
                            No slots created yet
                        </div>
                    </div>
                ) : (
                    slots.map((slot) => (
                        <div
                            className="col-md-4 mb-4"
                            key={slot.slotId}
                        >
                            <motion.div
                                className="card p-4 shadow-lg"
                                whileHover={{ scale: 1.03 }}
                            >
                                <h5>{slot.interviewDate}</h5>

                                <p>
                                    {slot.startTime} - {slot.endTime}
                                </p>

                                <p>
                                    Status: {slot.slotStatus}
                                </p>
                                <>
                                    <button
                                        className="btn btn-warning w-100 mb-2"
                                        onClick={() => {
                                            setEditingSlotId(slot.slotId);

                                            setSlotData({
                                                interviewerId: interviewerId,
                                                interviewDate: slot.interviewDate,
                                                startTime: slot.startTime,
                                                endTime: slot.endTime
                                            });

                                            window.scrollTo({
                                                top: 0,
                                                behavior: "smooth"
                                            });
                                        }}
                                    >
                                        Edit Slot
                                    </button>

                                    <button
                                        className="btn btn-danger w-100"
                                        onClick={() => {
                                            const confirmDelete = window.confirm(
                                                "Are you sure you want to delete this slot?"
                                            );

                                            if (confirmDelete) {
                                                handleDeleteSlot(slot.slotId);
                                            }
                                        }}
                                    >
                                        Delete Slot
                                    </button>
                                </>
                            </motion.div>
                        </div>
                    ))
                )}
            </div>

            {/* Bookings Section */}
            <div className="text-white mb-4">
                <h2>Scheduled Interviews</h2>
            </div>

            <div className="row">
                {bookings.length === 0 ? (
                    <div className="col-12">
                        <div className="alert alert-light text-center">
                            No interviews scheduled yet
                        </div>
                    </div>
                ) : (
                    bookings.map((booking) => (
                        <div
                            className="col-md-6 mb-4"
                            key={booking.bookingId}
                        >
                            <motion.div
                                className="card p-4 shadow-lg"
                                whileHover={{ scale: 1.03 }}
                            >
                                <h5>
                                    Candidate:
                                    {booking.candidateName}
                                </h5>

                                <p>
                                    Status:
                                    {booking.bookingStatus}
                                </p>

                                {booking.bookingStatus === "BOOKED" ? (
                                    <>
                            <textarea
                                className="form-control mb-2"
                                placeholder="Feedback comments"
                                value={feedbackData[booking.bookingId]?.comments || ""}
                                onChange={(e) =>
                                    handleFeedbackChange(
                                        booking.bookingId,
                                        "comments",
                                        e.target.value
                                    )
                                }
                            />

                                        <input
                                            type="number"
                                            className="form-control mb-2"
                                            placeholder="Rating (1-5)"
                                            value={feedbackData[booking.bookingId]?.rating || ""}
                                            onChange={(e) =>
                                                handleFeedbackChange(
                                                    booking.bookingId,
                                                    "rating",
                                                    e.target.value
                                                )
                                            }
                                        />

                                        <select
                                            className="form-select mb-2"
                                            value={feedbackData[booking.bookingId]?.recommendationStatus || ""}
                                            onChange={(e) =>
                                                handleFeedbackChange(
                                                    booking.bookingId,
                                                    "recommendationStatus",
                                                    e.target.value
                                                )
                                            }
                                        >
                                            <option value="">
                                                Select Recommendation
                                            </option>

                                            <option value="STRONG_HIRE">
                                                Strong Hire
                                            </option>

                                            <option value="HIRE">
                                                Hire
                                            </option>

                                            <option value="REJECT">
                                                Reject
                                            </option>
                                        </select>

                                        <button
                                            className="btn btn-success w-100"
                                            disabled={
                                                !feedbackData[booking.bookingId]?.rating ||
                                                !feedbackData[booking.bookingId]?.recommendationStatus
                                            }
                                            onClick={() =>
                                                handleSubmitFeedback(
                                                    booking.bookingId
                                                )
                                            }
                                        >
                                            Submit Feedback
                                        </button>
                                    </>
                                ) : (
                                    <div className="alert alert-secondary text-center mt-3">
                                        Feedback not required for this interview
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

export default InterviewerDashboard;