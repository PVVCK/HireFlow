import api from "./axiosConfig";

export const getDashboardStats = () => {
    return api.get("/recruiter/dashboard");
};

export const getAllCandidates = () => {
    return api.get("/recruiter/candidates");
};

export const getPendingCandidates = () => {
    return api.get("/recruiter/candidates/pending");
};

export const updateHiringDecision = (data) => {
    return api.put("/recruiter/decision", data);
};

export const getSelectedCandidates = () => {
    return api.get("/recruiter/candidates/selected");
};

export const getRejectedCandidates = () => {
    return api.get("/recruiter/candidates/rejected");
};