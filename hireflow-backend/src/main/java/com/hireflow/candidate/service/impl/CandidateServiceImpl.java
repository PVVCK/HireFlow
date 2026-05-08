package com.hireflow.candidate.service.impl;

import com.hireflow.candidate.dto.request.*;
import com.hireflow.candidate.dto.response.*;
import com.hireflow.candidate.service.CandidateService;
import com.hireflow.common.entity.*;
import com.hireflow.common.enums.*;
import com.hireflow.common.exception.custom.*;
import com.hireflow.common.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateServiceImpl implements CandidateService {

    private final UserRepository userRepository;
    private final CandidateProfileRepository profileRepository;
    private final InterviewSlotRepository slotRepository;
    private final InterviewBookingRepository bookingRepository;


    @Override
    public CandidateProfileResponse getProfile(Long userId) {

        log.info(
                "Fetching candidate profile for userId: {}",
                userId
        );

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        CandidateProfile profile =
                profileRepository.findByUserWithDetails(user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Candidate profile not found"
                                )
                        );

        log.info(
                "Candidate profile fetched successfully for userId: {}",
                userId
        );

        return mapProfile(profile);
    }

    @Override
    @Transactional
    public CandidateProfileResponse createOrUpdateProfile(
            Long userId,
            CandidateProfileRequest request
    ) {

        log.info("Creating/updating profile for user id: {}", userId);

        User user = getUser(userId);
        validateCandidateRole(user);

        CandidateProfile profile = profileRepository.findByUser(user)
                .orElse(new CandidateProfile());

        profile.setUser(user);
        profile.setSkills(request.getSkills());
        profile.setExperienceYears(request.getExperienceYears());
        profile.setResumeUrl(request.getResumeUrl());
        profile.setApplicationStatus(ApplicationStatus.APPLIED);

        CandidateProfile saved = profileRepository.save(profile);

        log.info("Candidate profile saved successfully for user id: {}", userId);

        return mapProfile(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailableSlotResponse> getAvailableSlots() {

        log.info("Fetching available interview slots");

        return slotRepository.findBySlotStatus(SlotStatus.AVAILABLE)
                .stream()
                .map(this::mapSlot)
                .toList();
    }


    @Override
    @Transactional
    public BookingResponse bookSlot(BookingRequest request) {

        log.info("Booking slot {} for candidate {}",
                request.getSlotId(),
                request.getCandidateId());

        User candidate = getUser(request.getCandidateId());
        validateCandidateRole(candidate);

        InterviewSlot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> {
                    log.error("Slot not found with id: {}", request.getSlotId());
                    return new ResourceNotFoundException("Slot not found");
                });

        if (slot.getSlotStatus() != SlotStatus.AVAILABLE) {
            log.error("Slot already booked: {}", slot.getId());
            throw new SlotUnavailableException("Slot already booked");
        }

        InterviewBooking booking = InterviewBooking.builder()
                .candidate(candidate)
                .interviewSlot(slot)
                .bookingStatus(BookingStatus.BOOKED)
                .scheduledAt(LocalDateTime.now())
                .build();

        slot.setSlotStatus(SlotStatus.BOOKED);
        slotRepository.save(slot);

        InterviewBooking savedBooking = bookingRepository.save(booking);

        log.info("Slot booked successfully. Booking id: {}", savedBooking.getId());

        return BookingResponse.builder()
                .bookingId(savedBooking.getId())
                .candidateId(candidate.getId())
                .slotId(slot.getId())
                .bookingStatus(savedBooking.getBookingStatus().name())
                .scheduledAt(savedBooking.getScheduledAt().toString())
                .build();
    }
    @Override
    @Transactional
    public BookingResponse rescheduleBooking(
            RescheduleBookingRequest request
    ) {

        log.info("Rescheduling booking id: {}", request.getBookingId());

        InterviewBooking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> {
                    log.error("Booking not found: {}", request.getBookingId());
                    return new ResourceNotFoundException("Booking not found");
                });

        InterviewSlot newSlot = slotRepository.findById(request.getNewSlotId())
                .orElseThrow(() -> {
                    log.error("New slot not found: {}", request.getNewSlotId());
                    return new ResourceNotFoundException("New slot not found");
                });

        if (newSlot.getSlotStatus() != SlotStatus.AVAILABLE) {
            log.error("New slot already booked: {}", newSlot.getId());
            throw new SlotUnavailableException("New slot already booked");
        }

        booking.getInterviewSlot().setSlotStatus(SlotStatus.AVAILABLE);
        newSlot.setSlotStatus(SlotStatus.BOOKED);

        booking.setInterviewSlot(newSlot);
        booking.setBookingStatus(BookingStatus.RESCHEDULED);

        InterviewBooking updatedBooking = bookingRepository.save(booking);

        log.info("Booking rescheduled successfully for booking id: {}",
                updatedBooking.getId());

        return BookingResponse.builder()
                .bookingId(updatedBooking.getId())
                .candidateId(updatedBooking.getCandidate().getId())
                .slotId(newSlot.getId())
                .bookingStatus(updatedBooking.getBookingStatus().name())
                .scheduledAt(updatedBooking.getScheduledAt().toString())
                .build();
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {

        log.info("Cancelling booking id: {}", bookingId);

        InterviewBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.error("Booking not found: {}", bookingId);
                    return new ResourceNotFoundException("Booking not found");
                });

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            log.error("Booking already cancelled: {}", bookingId);
            throw new InvalidBookingException("Booking already cancelled");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.getInterviewSlot().setSlotStatus(SlotStatus.AVAILABLE);

        bookingRepository.save(booking);

        log.info("Booking cancelled successfully: {}", bookingId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getCandidateBookings(Long candidateId) {

        log.info("Fetching bookings for candidate id: {}", candidateId);

        User candidate = getUser(candidateId);

        validateCandidateRole(candidate);

        List<InterviewBooking> bookings =
                bookingRepository.findByCandidate(candidate);

        return bookings.stream()
                .map(this::mapBooking)
                .toList();
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void validateCandidateRole(User user) {
        if (user.getRole() != Role.CANDIDATE) {
            throw new UnauthorizedActionException(
                    "Only candidates can perform this action"
            );
        }
    }

    private CandidateProfileResponse mapProfile(
            CandidateProfile profile
    ) {
        return CandidateProfileResponse.builder()
                .candidateId(profile.getId())
                .name(profile.getUser().getName())
                .email(profile.getUser().getEmail())
                .skills(profile.getSkills())
                .experienceYears(profile.getExperienceYears())
                .resumeUrl(profile.getResumeUrl())
                .applicationStatus(profile.getApplicationStatus().name())
                .build();
    }

    private AvailableSlotResponse mapSlot(InterviewSlot slot) {
        return AvailableSlotResponse.builder()
                .slotId(slot.getId())
                .interviewerName(slot.getInterviewer().getName())
                .interviewDate(slot.getInterviewDate().toString())
                .startTime(slot.getStartTime().toString())
                .endTime(slot.getEndTime().toString())
                .slotStatus(slot.getSlotStatus().name())
                .build();
    }

    private BookingResponse mapBooking(InterviewBooking booking) {
        return BookingResponse.builder()
                .bookingId(booking.getId())
                .candidateId(booking.getCandidate().getId())
                .slotId(booking.getInterviewSlot().getId())
                .bookingStatus(booking.getBookingStatus().name())
                .scheduledAt(booking.getScheduledAt().toString())
                .build();
    }
}