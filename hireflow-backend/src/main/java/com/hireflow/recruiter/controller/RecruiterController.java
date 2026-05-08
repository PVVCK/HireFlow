package com.hireflow.recruiter.controller;

import com.hireflow.common.response.ApiResponse;
import com.hireflow.recruiter.dto.request.HiringDecisionRequest;
import com.hireflow.recruiter.dto.response.*;
import com.hireflow.recruiter.service.RecruiterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/recruiter")
@RequiredArgsConstructor
public class RecruiterController {

    private final RecruiterService recruiterService;

    @GetMapping("/candidates")
    public ApiResponse<List<CandidateDashboardResponse>> getCandidates(
            HttpServletRequest request) {

        return buildResponse(
                recruiterService.getAllCandidates(),
                "Candidates fetched successfully",
                request.getRequestURI()
        );
    }

    @GetMapping("/candidates/pending")
    public ApiResponse<List<CandidateDashboardResponse>> getPendingCandidates(
            HttpServletRequest request) {

        return buildResponse(
                recruiterService.getPendingCandidates(),
                "Pending candidates fetched successfully",
                request.getRequestURI()
        );
    }

    @PutMapping("/decision")
    public ApiResponse<CandidateDashboardResponse> updateDecision(
            @Valid @RequestBody HiringDecisionRequest request,
            HttpServletRequest httpRequest) {

        return buildResponse(
                recruiterService.updateHiringDecision(request),
                "Hiring decision updated successfully",
                httpRequest.getRequestURI()
        );
    }

    @GetMapping("/dashboard")
    public ApiResponse<RecruiterDashboardResponse> dashboard(
            HttpServletRequest request) {

        return buildResponse(
                recruiterService.getDashboardStats(),
                "Dashboard fetched successfully",
                request.getRequestURI()
        );
    }

    @GetMapping("/candidates/selected")
    public ApiResponse<List<CandidateDashboardResponse>> getSelectedCandidates(
            HttpServletRequest request) {

        return buildResponse(
                recruiterService.getSelectedCandidates(),
                "Selected candidates fetched successfully",
                request.getRequestURI()
        );
    }

    @GetMapping("/candidates/rejected")
    public ApiResponse<List<CandidateDashboardResponse>> getRejectedCandidates(
            HttpServletRequest request) {

        return buildResponse(
                recruiterService.getRejectedCandidates(),
                "Rejected candidates fetched successfully",
                request.getRequestURI()
        );
    }


    private <T> ApiResponse<T> buildResponse(
            T data,
            String message,
            String path) {

        return ApiResponse.<T>builder()
                .success(true)
                .statusCode(200)
                .message(message)
                .data(data)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
}