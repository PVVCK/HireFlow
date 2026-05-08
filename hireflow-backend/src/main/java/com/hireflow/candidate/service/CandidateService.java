package com.hireflow.candidate.service;

import com.hireflow.candidate.dto.request.BookingRequest;
import com.hireflow.candidate.dto.request.CandidateProfileRequest;
import com.hireflow.candidate.dto.request.RescheduleBookingRequest;
import com.hireflow.candidate.dto.response.AvailableSlotResponse;
import com.hireflow.candidate.dto.response.BookingResponse;
import com.hireflow.candidate.dto.response.CandidateProfileResponse;

import java.util.List;

public interface CandidateService
{

    CandidateProfileResponse createOrUpdateProfile(Long userId, CandidateProfileRequest request);

    List<AvailableSlotResponse> getAvailableSlots();

    BookingResponse bookSlot(BookingRequest request);

    BookingResponse rescheduleBooking(RescheduleBookingRequest request);

    void cancelBooking(Long bookingId);

    List<BookingResponse> getCandidateBookings(Long candidateId);

    CandidateProfileResponse getProfile(Long userId);
}
