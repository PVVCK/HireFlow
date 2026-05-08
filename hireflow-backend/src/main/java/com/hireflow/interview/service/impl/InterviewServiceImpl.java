package com.hireflow.interview.service.impl;

import com.hireflow.common.entity.*;
import com.hireflow.common.enums.*;
import com.hireflow.common.exception.custom.*;
import com.hireflow.common.repository.*;
import com.hireflow.interview.dto.request.*;
import com.hireflow.interview.dto.response.*;
import com.hireflow.interview.service.InterviewService;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final UserRepository userRepository;
    private final InterviewSlotRepository slotRepository;
    private final InterviewBookingRepository bookingRepository;
    private final FeedbackRepository feedbackRepository;

    @Override
    public SlotResponse createSlot(CreateSlotRequest request) {
        log.info("Creating interview slot for interviewer id: {}",
                request.getInterviewerId());

        User interviewer = userRepository.findById(request.getInterviewerId())
                .orElseThrow(() -> {
                    log.error("Interviewer not found with id: {}",
                            request.getInterviewerId());
                    return new ResourceNotFoundException("Interviewer not found");
                });

        InterviewSlot slot = InterviewSlot.builder()
                .interviewer(interviewer)
                .interviewDate(request.getInterviewDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .slotStatus(SlotStatus.AVAILABLE)
                .build();

        InterviewSlot savedSlot = slotRepository.save(slot);

        log.info("Interview slot created successfully with id: {}",
                savedSlot.getId());


        return mapSlot(savedSlot);
    }

    @Override
    @Transactional
    public SlotResponse updateSlot(Long slotId, UpdateSlotRequest request) {

        log.info("Updating interview slot id: {}", slotId);

        InterviewSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> {
                    log.error("Slot not found with id: {}", slotId);
                    return new ResourceNotFoundException("Slot not found");
                });

        slot.setInterviewDate(request.getInterviewDate());
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());

        return mapSlot(slotRepository.save(slot));
    }

    @Override
    public void deleteSlot(Long slotId) {
        log.info("Deleting slot with id: {}", slotId);

        InterviewSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> {
                    log.error("Slot not found with id: {}", slotId);
                    return new ResourceNotFoundException("Slot not found");
                });

        slotRepository.delete(slot);

        log.info("Slot deleted successfully with id: {}", slotId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewBookingResponse> getScheduledBookings(Long interviewerId) {

        log.info("Fetching scheduled bookings for interviewer id: {}",
                interviewerId);

        User interviewer = userRepository.findById(interviewerId)
                .orElseThrow(() -> {
                    log.error("Interviewer not found with id: {}", interviewerId);
                    return new ResourceNotFoundException("Interviewer not found");
                });


        List<InterviewSlot> slots = slotRepository.findByInterviewer(interviewer);

        return slots.stream()
                .flatMap(slot -> bookingRepository.findByInterviewSlot(slot).stream())
                .map(this::mapBooking)
                .toList();
    }
    @Override
    public FeedbackResponse submitFeedback(FeedbackRequest request) {

        log.info("Submitting feedback for booking id: {}",
                request.getBookingId());

        InterviewBooking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> {
                    log.error("Booking not found with id: {}",
                            request.getBookingId());
                    return new ResourceNotFoundException("Booking not found");
                });

        if (feedbackRepository.existsByInterviewBooking(booking)) {
            log.error("Feedback already exists for booking id: {}",
                    request.getBookingId());

            throw new DuplicateResourceException(
                    "Feedback already submitted for this booking"
            );
        }

        Feedback feedback = Feedback.builder()
                .interviewBooking(booking)
                .rating(request.getRating())
                .comments(request.getComments())
                .recommendationStatus(request.getRecommendationStatus())
                .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);

        log.info("Feedback submitted successfully with id: {}",
                savedFeedback.getId());

        return mapFeedback(savedFeedback);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SlotResponse> getInterviewerSlots(Long interviewerId) {

        log.info("Fetching slots for interviewer {}", interviewerId);

        User interviewer = userRepository.findById(interviewerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Interviewer not found"));

        List<InterviewSlot> slots =
                slotRepository.findByInterviewer(interviewer);

        return slots.stream()
                .map(this::mapSlot)
                .toList();
    }

    private SlotResponse mapSlot(InterviewSlot slot) {
        return SlotResponse.builder()
                .slotId(slot.getId())
                .interviewerName(slot.getInterviewer().getName())
                .interviewDate(slot.getInterviewDate().toString())
                .startTime(slot.getStartTime().toString())
                .endTime(slot.getEndTime().toString())
                .slotStatus(slot.getSlotStatus().name())
                .build();
    }

    private InterviewBookingResponse mapBooking(InterviewBooking booking){
        return InterviewBookingResponse.builder()
                .bookingId(booking.getId())
                .candidateName(booking.getCandidate().getName())
                .bookingStatus(booking.getBookingStatus().name())
                .build();
    }

    private FeedbackResponse mapFeedback(Feedback feedback){
        return FeedbackResponse.builder()
                .feedbackId(feedback.getId())
                .rating(feedback.getRating())
                .comments(feedback.getComments())
                .recommendationStatus(feedback.getRecommendationStatus().name())
                .build();
    }
}
