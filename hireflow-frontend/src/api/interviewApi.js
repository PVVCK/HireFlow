import api from "./axiosConfig";

export const createSlot = (data) => {
    return api.post("/interviews/slots", data);
};

export const updateSlot = (slotId, data) => {
    return api.put(`/interviews/slots/${slotId}`, data);
};

export const deleteSlot = (slotId) => {
    return api.delete(`/interviews/slots/${slotId}`);
};

export const getBookings = (interviewerId) => {
    return api.get(`/interviews/${interviewerId}/bookings`);
};

export const submitFeedback = (data) => {
    return api.post("/interviews/feedback", data);
};

export const getInterviewerSlots = (interviewerId) => {
    return api.get(`/interviews/${interviewerId}/slots`);
};