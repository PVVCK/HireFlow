import api from "./axiosConfig";

export const createProfile = (userId, data) => {
    return api.post(`/candidates/${userId}/profile`, data);
};

export const getAvailableSlots = () => {
    return api.get("/candidates/slots");
};

export const bookSlot = (data) => {
    return api.post("/candidates/bookings", data);
};

export const rescheduleBooking = (data) => {
    return api.put("/candidates/bookings/reschedule", data);
};

export const cancelBooking = (bookingId) => {
    return api.delete(`/candidates/bookings/${bookingId}`);
};

export const getCandidateBookings = (candidateId) => {
    return api.get(`/candidates/${candidateId}/bookings`);
};

export const getCandidateProfile = (userId) => {
    return api.get(`/candidates/${userId}/profile`);
};