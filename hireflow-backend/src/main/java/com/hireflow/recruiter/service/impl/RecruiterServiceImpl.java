package com.hireflow.recruiter.service.impl;

import com.hireflow.common.entity.CandidateProfile;
import com.hireflow.common.entity.Feedback;
import com.hireflow.common.enums.ApplicationStatus;
import com.hireflow.common.exception.custom.ResourceNotFoundException;
import com.hireflow.common.repository.CandidateProfileRepository;
import com.hireflow.common.repository.FeedbackRepository;
import com.hireflow.common.repository.InterviewBookingRepository;
import com.hireflow.recruiter.dto.request.HiringDecisionRequest;
import com.hireflow.recruiter.dto.response.*;
import com.hireflow.recruiter.service.RecruiterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecruiterServiceImpl implements RecruiterService {

    private final CandidateProfileRepository profileRepository;
    private final InterviewBookingRepository bookingRepository;
    private final FeedbackRepository feedbackRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CandidateDashboardResponse> getAllCandidates() {

        log.info("Fetching all candidate profiles for recruiter dashboard");

        return profileRepository.findAll()
                .stream()
                .map(profile -> {

                    String rejectionReason = null;

                    if (profile.getApplicationStatus() == ApplicationStatus.REJECTED) {

                        rejectionReason = bookingRepository
                                .findByCandidate(profile.getUser())
                                .stream()
                                .findFirst()
                                .flatMap(feedbackRepository::findByInterviewBooking)
                                .map(Feedback::getComments)
                                .orElse("No feedback available");
                    }

                    return CandidateDashboardResponse.builder()
                            .candidateId(profile.getId())
                            .candidateName(profile.getUser().getName())
                            .email(profile.getUser().getEmail())
                            .applicationStatus(profile.getApplicationStatus().name())
                            .rejectionReason(rejectionReason)
                            .build();
                })
                .toList();
    }
    @Override
    @Transactional
    public CandidateDashboardResponse updateHiringDecision(HiringDecisionRequest request) {

        log.info("Updating hiring decision for candidate id: {}", request.getCandidateId());

        CandidateProfile profile = profileRepository.findById(request.getCandidateId())
                .orElseThrow(() -> {
                    log.error("Candidate not found with id: {}",
                            request.getCandidateId());
                    return new ResourceNotFoundException("Candidate not found");
                });

        profile.setApplicationStatus(request.getApplicationStatus());

        CandidateProfile saved = profileRepository.save(profile);

        log.info("Successfully updated candidate {} to status {}",
                saved.getId(),
                saved.getApplicationStatus());

        return CandidateDashboardResponse.builder()
                .candidateId(saved.getId())
                .candidateName(saved.getUser().getName())
                .email(saved.getUser().getEmail())
                .applicationStatus(saved.getApplicationStatus().name())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public RecruiterDashboardResponse getDashboardStats() {

        log.info("Fetching recruiter dashboard statistics");

        long totalCandidates = profileRepository.count();

        List<CandidateProfile> profiles = profileRepository.findAll();

        long selectedCandidates = profiles.stream()
                .filter(c -> c.getApplicationStatus() == ApplicationStatus.SELECTED)
                .count();

        long rejectedCandidates = profiles.stream()
                .filter(c -> c.getApplicationStatus() == ApplicationStatus.REJECTED)
                .count();

        log.info("Dashboard stats fetched successfully");

        return RecruiterDashboardResponse.builder()
                .totalCandidates(totalCandidates)
                .totalScheduledInterviews(bookingRepository.count())
                .selectedCandidates(selectedCandidates)
                .rejectedCandidates(rejectedCandidates)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidateDashboardResponse> getSelectedCandidates() {

        log.info("Fetching selected candidates");

        List<CandidateProfile> selectedProfiles =
                profileRepository.findByApplicationStatus(ApplicationStatus.SELECTED);

        return selectedProfiles.stream()
                .map(profile -> CandidateDashboardResponse.builder()
                        .candidateId(profile.getId())
                        .candidateName(profile.getUser().getName())
                        .email(profile.getUser().getEmail())
                        .skills(profile.getSkills())
                        .experienceYears(profile.getExperienceYears())
                        .applicationStatus(profile.getApplicationStatus().name())
                        .build())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidateDashboardResponse> getRejectedCandidates() {

        log.info("Fetching rejected candidates");

        List<CandidateProfile> rejectedProfiles =
                profileRepository.findByApplicationStatus(ApplicationStatus.REJECTED);

        return rejectedProfiles.stream()
                .map(profile -> {

                    String rejectionReason = bookingRepository
                            .findByCandidate(profile.getUser())
                            .stream()
                            .findFirst()
                            .flatMap(feedbackRepository::findByInterviewBooking)
                            .map(Feedback::getComments)
                            .orElse("No feedback available");

                    return CandidateDashboardResponse.builder()
                            .candidateId(profile.getId())
                            .candidateName(profile.getUser().getName())
                            .email(profile.getUser().getEmail())
                            .skills(profile.getSkills())
                            .experienceYears(profile.getExperienceYears())
                            .applicationStatus(profile.getApplicationStatus().name())
                            .rejectionReason(rejectionReason)
                            .build();
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidateDashboardResponse> getPendingCandidates() {

        List<CandidateProfile> profiles = profileRepository.findAll();

        return profiles.stream()
                .filter(profile ->
                        profile.getApplicationStatus() == ApplicationStatus.APPLIED ||
                                profile.getApplicationStatus() == ApplicationStatus.INTERVIEW_SCHEDULED
                )
                .map(this::mapCandidateResponse)
                .toList();
    }

    private CandidateDashboardResponse mapCandidateResponse(
            CandidateProfile profile) {

        return CandidateDashboardResponse.builder()
                .candidateId(profile.getId())
                .candidateName(profile.getUser().getName())
                .email(profile.getUser().getEmail())
                .skills(profile.getSkills())
                .experienceYears(profile.getExperienceYears())
                .applicationStatus(
                        profile.getApplicationStatus().name()
                )
                .build();
    }
}