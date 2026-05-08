package com.hireflow.recruiter.service;

import com.hireflow.recruiter.dto.request.HiringDecisionRequest;
import com.hireflow.recruiter.dto.response.CandidateDashboardResponse;
import com.hireflow.recruiter.dto.response.RecruiterDashboardResponse;

import java.util.List;

public interface RecruiterService {

    List<CandidateDashboardResponse> getAllCandidates();

    CandidateDashboardResponse updateHiringDecision(HiringDecisionRequest request);

    RecruiterDashboardResponse getDashboardStats();

    List<CandidateDashboardResponse> getSelectedCandidates();

    List<CandidateDashboardResponse> getRejectedCandidates();

    List<CandidateDashboardResponse> getPendingCandidates();

}
