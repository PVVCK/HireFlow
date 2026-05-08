import { useEffect, useState } from "react";
import {
    createProfile,
    getAvailableSlots,
    bookSlot,
    getCandidateBookings,
    cancelBooking,
    rescheduleBooking
} from "../api/candidateApi";

import { motion } from "framer-motion";
import {
    UserCircle,
    Calendar,
    Briefcase,
    FileText
} from "lucide-react";

import { toast } from "react-toastify";
import { getCandidateProfile } from "../api/candidateApi";

function CandidateDashboard() {

    const [profileData, setProfileData] = useState({
        skills: "",
        experienceYears: "",
        resumeUrl: ""
    });

    const [slots, setSlots] = useState([]);
    const [bookings, setBookings] = useState([]);
    const [selectedBookingId, setSelectedBookingId] = useState(null);
    const [newSlotId, setNewSlotId] = useState("");
    const [loading, setLoading] = useState(true);
    const [profileCompleted, setProfileCompleted] = useState(false);
    const [profile, setProfile] = useState(null);
    const [isEditingProfile, setIsEditingProfile] = useState(true);

    const userId = Number(localStorage.getItem("userId"));

    useEffect(() => {
        const loadCandidateDashboard = async () => {
            try {
                setLoading(true);

                await fetchProfile();
                await fetchSlots();
                await fetchBookings();

            } finally {
                setLoading(false);
            }
        };

        loadCandidateDashboard();
    }, []);

    const fetchSlots = async () => {
        try {
            const response = await getAvailableSlots();
            setSlots(response.data.data);
        } catch (error) {
            console.error(error);
        }
    };

    const fetchBookings = async () => {
        try {
            const response = await getCandidateBookings(userId);
            setBookings(response.data.data);
        } catch (error) {
            console.error(error);
        }
    };

    const handleProfileChange = (e) => {
        setProfileData({
            ...profileData,
            [e.target.name]: e.target.value
        });
    };
    const fetchProfile = async () => {
        try {
            const response = await getCandidateProfile(userId);

            setProfile(response.data.data);
            setProfileCompleted(true);
            setIsEditingProfile(false);

        } catch (error) {

            if (error.response?.status === 404) {

                setProfileCompleted(false);
                setIsEditingProfile(true);
            } else {

                console.error("Error fetching profile:", error);
            }
        }
    };

    const handleProfileSubmit = async (e) => {
        e.preventDefault();

        try {
            await createProfile(userId, profileData);

            toast.success("Profile saved successfully");

            await fetchProfile();
            setProfileData({               // ✅ ADD HERE
                skills: "",
                experienceYears: "",
                resumeUrl: ""
            });
            setIsEditingProfile(false);        // 🔥 IMPORTANT

        } catch (error) {
            console.error(error);
            toast.error("Profile creation failed");
        }
    };

    const handleBookSlot = async (slotId) => {
        try {
            await bookSlot({
                candidateId: userId,
                slotId: slotId
            });

            toast.success("Interview booked successfully");
            fetchSlots();
            fetchBookings();

        } catch (error) {
            console.error(error);
            toast.error("Booking failed");
        }
    };

    const handleCancelBooking = async (bookingId) => {
        try {
            await cancelBooking(bookingId);

            toast.success("Interview cancelled successfully");

            fetchBookings();
            fetchSlots();

        } catch (error) {
            console.error(error);
            toast.error("Cancellation failed");
        }
    };

    const handleRescheduleBooking = async (bookingId) => {
        if (!newSlotId) {
            toast.error("Please select a new slot");
            return;
        }

        try {
            await rescheduleBooking({
                bookingId: bookingId,
                newSlotId: Number(newSlotId)
            });

            toast.success("Interview rescheduled successfully");

            setSelectedBookingId(null);
            setNewSlotId("");

            fetchBookings();
            fetchSlots();

        } catch (error) {
            console.error(error);
            toast.error("Reschedule failed");
        }
    };

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

                    <h4>Loading Candidate Dashboard...</h4>
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
                    "linear-gradient(135deg, #0f172a, #1e293b, #111827)"
            }}
        >
            {/* Hero Section */}
            <motion.div
                className="text-center text-white mb-5"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
            >
                <h1 className="display-4 fw-bold">
                    Candidate Career Hub
                </h1>

                <p className="lead">
                    Build your profile • Book interviews • Track your hiring journey
                </p>
            </motion.div>

            {/* Metrics Section */}
            <div className="row mb-5">
                <div className="col-md-4">
                    <motion.div
                        whileHover={{ scale: 1.05 }}
                        className="card shadow-lg p-4 text-center"
                        style={{
                            borderRadius: "20px",
                            background: "rgba(255,255,255,0.1)",
                            color: "white"
                        }}
                    >
                        <Briefcase size={40} />
                        <h5 className="mt-3">Total Interviews</h5>
                        <h2>{bookings.length}</h2>
                        <p className="text-light">Bookings made</p>
                    </motion.div>
                </div>

                <div className="col-md-4">
                    <motion.div
                        whileHover={{ scale: 1.05 }}
                        className="card shadow-lg p-4 text-center"
                        style={{
                            borderRadius: "20px",
                            background: "rgba(255,255,255,0.1)",
                            color: "white"
                        }}
                    >
                        <Calendar size={40} />
                        <h5 className="mt-3">Available Slots</h5>
                        <h2>{slots.length}</h2>
                    </motion.div>
                </div>

                <div className="col-md-4">
                    <motion.div
                        whileHover={{ scale: 1.05 }}
                        className="card shadow-lg p-4 text-center"
                        style={{
                            borderRadius: "20px",
                            background: "rgba(255,255,255,0.1)",
                            color: "white"
                        }}
                    >
                        <FileText size={40} />
                        <h5 className="mt-3">Resume Status</h5>
                        <h2>
                            {profileCompleted ? "Uploaded" : "Pending"}
                        </h2>
                    </motion.div>
                </div>
            </div>

            {!profileCompleted || isEditingProfile ? (
                <div className="row justify-content-center mb-5">
                    <div className="col-md-8">
                        <motion.div
                            whileHover={{ scale: 1.02 }}
                            className="card shadow-lg p-5"
                            style={{ borderRadius: "20px" }}
                        >
                            <h3 className="text-center mb-4">
                                <UserCircle size={35} /> {profileCompleted ? "Edit Profile" : "Complete Profile"}
                            </h3>

                            <form onSubmit={handleProfileSubmit}>
                                <input
                                    type="text"
                                    className="form-control mb-3"
                                    placeholder="Skills"
                                    name="skills"
                                    value={profileData.skills}
                                    onChange={handleProfileChange}
                                />

                                <input
                                    type="number"
                                    className="form-control mb-3"
                                    placeholder="Experience Years"
                                    name="experienceYears"
                                    value={profileData.experienceYears}
                                    onChange={handleProfileChange}
                                />

                                <input
                                    type="text"
                                    className="form-control mb-3"
                                    placeholder="Resume URL"
                                    name="resumeUrl"
                                    value={profileData.resumeUrl}
                                    onChange={handleProfileChange}
                                />

                                <button className="btn btn-dark w-100">
                                    Save Profile
                                </button>
                            </form>
                        </motion.div>
                    </div>
                </div>
            ) : (
                <div className="row justify-content-center mb-5">
                    <div className="col-md-8">
                        <motion.div
                            whileHover={{ scale: 1.02 }}
                            className="card shadow-lg p-5"
                            style={{ borderRadius: "20px" }}
                        >
                            <h3 className="text-center mb-4">
                                <UserCircle size={35} /> Your Profile
                            </h3>

                            <p><strong>Name:</strong> {profile.name}</p>
                            <p><strong>Email:</strong> {profile.email}</p>
                            <p><strong>Skills:</strong> {profile.skills}</p>
                            <p><strong>Experience:</strong> {profile.experienceYears} years</p>
                            <p><strong>Status:</strong> {profile.applicationStatus}</p>

                            <button
                                className="btn btn-outline-dark w-100 mt-3"
                                onClick={() => {
                                    setProfileData({
                                        skills: profile.skills,
                                        experienceYears: profile.experienceYears,
                                        resumeUrl: profile.resumeUrl
                                    });
                                    setIsEditingProfile(true);
                                }}
                            >
                                Edit Profile
                            </button>
                        </motion.div>
                    </div>
                </div>
            )}

            {/* Slots Section */}
            <div className="text-white mb-4">
                <h2>Available Interview Slots</h2>
            </div>

            <div className="row">
                {slots.length === 0 ? (
                    <div className="col-12">
                        <div className="alert alert-light text-center">
                            No interview slots available right now
                        </div>
                    </div>
                ) : (
                    slots.map((slot) => (
                    <div
                        className="col-md-4 mb-4"
                        key={slot.slotId}
                    >
                        <motion.div
                            whileHover={{ scale: 1.05 }}
                            className="card shadow-lg p-4"
                            style={{
                                borderRadius: "20px"
                            }}
                        >
                            <h5>{slot.interviewerName}</h5>

                            <p>
                                Date: {slot.interviewDate}
                            </p>

                            <p>
                                Time: {slot.startTime} - {slot.endTime}
                            </p>

                            <button
                                className="btn btn-primary w-100"
                                disabled={!profileCompleted}
                                onClick={() => handleBookSlot(slot.slotId)}
                            >
                                {profileCompleted ? "Book Interview" : "Complete Profile First"}
                            </button>
                        </motion.div>
                    </div>
                ))
                )}
            </div>

            {/* Interview Timeline Section */}
            <div className="text-white mt-5 mb-4">
                <h2>Your Interview Timeline</h2>
                <p className="text-light">
                    Track all your booked interviews
                </p>
            </div>

            <div className="row">
                {bookings.length === 0 ? (
                    <div className="col-12">
                        <div className="alert alert-light text-center p-5">
                            <h5>No interviews yet</h5>
                            <p>Book your first interview to get started</p>
                        </div>
                    </div>
                ) : (
                    bookings.map((booking) => (
                    <div
                        className="col-md-6 mb-4"
                        key={booking.bookingId}
                    >
                        <motion.div
                            whileHover={{ scale: 1.03 }}
                            className="card shadow-lg p-4"
                            style={{
                                borderRadius: "20px"
                            }}
                        >
                            <h5 className="fw-bold">
                                Booking #{booking.bookingId}
                            </h5>

                            <p>Slot ID: {booking.slotId}</p>

                            <p>
                                Scheduled At: {booking.scheduledAt}
                            </p>

                            <div className="mb-3">
                    <span
                        className={`badge ${
                            booking.bookingStatus === "BOOKED"
                                ? "bg-success"
                                : "bg-danger"
                        }`}
                    >
                        {booking.bookingStatus}
                    </span>
                            </div>

                            {["BOOKED", "CONFIRMED", "SCHEDULED", "RESCHEDULED"].includes(booking.bookingStatus) && (
                                <>
                                    <button
                                        className="btn btn-warning w-100 mb-2"
                                        onClick={() =>
                                            setSelectedBookingId(booking.bookingId)
                                        }
                                    >
                                        Reschedule Interview
                                    </button>

                                    <button
                                        className="btn btn-danger w-100"
                                        onClick={() => {
                                            const confirmCancel = window.confirm(
                                                "Are you sure you want to cancel this interview?"
                                            );

                                            if (confirmCancel) {
                                                handleCancelBooking(
                                                    booking.bookingId
                                                );
                                            }
                                        }}
                                    >
                                        Cancel Interview
                                    </button>
                                </>
                            )}

                            {selectedBookingId === booking.bookingId && (
                                <div className="mt-3">
                                    <select
                                        className="form-select mb-2"
                                        value={newSlotId}
                                        onChange={(e) =>
                                            setNewSlotId(e.target.value)
                                        }
                                    >
                                        <option value="">
                                            Select New Slot
                                        </option>

                                        {slots.map((slot) => (
                                            <option
                                                key={slot.slotId}
                                                value={slot.slotId}
                                            >
                                                {slot.interviewerName} | {slot.interviewDate} | {slot.startTime} - {slot.endTime}
                                            </option>
                                        ))}
                                    </select>

                                    <button
                                        className="btn btn-primary w-100"
                                        onClick={() =>
                                            handleRescheduleBooking(
                                                booking.bookingId
                                            )
                                        }
                                    >
                                        Confirm Reschedule
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

export default CandidateDashboard;