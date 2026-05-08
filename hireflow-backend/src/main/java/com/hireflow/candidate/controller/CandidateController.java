package com.hireflow.candidate.controller;

import com.hireflow.candidate.dto.request.*;
import com.hireflow.candidate.dto.response.*;
import com.hireflow.candidate.service.CandidateService;
import com.hireflow.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping("/{userId}/profile")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> createProfile(
            @PathVariable Long userId,
            @Valid @RequestBody CandidateProfileRequest request,
            HttpServletRequest httpRequest) {

        CandidateProfileResponse response =
                candidateService.createOrUpdateProfile(userId, request);

        return ResponseEntity.ok(buildResponse(response,
                "Profile updated successfully",
                httpRequest.getRequestURI()));
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> getProfile(
            @PathVariable Long userId,
            HttpServletRequest request
    ) {

        CandidateProfileResponse response =
                candidateService.getProfile(userId);

        return ResponseEntity.ok(
                buildResponse(
                        response,
                        "Profile fetched successfully",
                        request.getRequestURI()
                )
        );
    }

    @GetMapping("/slots")
    public ResponseEntity<ApiResponse<List<AvailableSlotResponse>>> getSlots(
            HttpServletRequest request) {

        List<AvailableSlotResponse> response =
                candidateService.getAvailableSlots();

        return ResponseEntity.ok(buildResponse(
                response,
                "Available slots fetched successfully",
                request.getRequestURI()
        ));
    }

    @PostMapping("/bookings")
    public ResponseEntity<ApiResponse<BookingResponse>> bookSlot(
            @Valid @RequestBody BookingRequest request,
            HttpServletRequest httpRequest) {

        BookingResponse response =
                candidateService.bookSlot(request);

        return ResponseEntity.ok(buildResponse(
                response,
                "Slot booked successfully",
                httpRequest.getRequestURI()
        ));
    }

    @PutMapping("/bookings/reschedule")
    public ResponseEntity<ApiResponse<BookingResponse>> reschedule(
            @Valid @RequestBody RescheduleBookingRequest request,
            HttpServletRequest httpRequest) {

        BookingResponse response =
                candidateService.rescheduleBooking(request);

        return ResponseEntity.ok(buildResponse(
                response,
                "Booking rescheduled successfully",
                httpRequest.getRequestURI()
        ));
    }

    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<ApiResponse<String>> cancelBooking(
            @PathVariable Long bookingId,
            HttpServletRequest request) {

        candidateService.cancelBooking(bookingId);

        return ResponseEntity.status(204).body((buildResponse(
                "Booking cancelled successfully",
                "Booking cancelled successfully",
                request.getRequestURI()
        )));
    }

    @GetMapping("/{candidateId}/bookings")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getCandidateBookings(
            @PathVariable Long candidateId,
            HttpServletRequest request) {

        List<BookingResponse> response =
                candidateService.getCandidateBookings(candidateId);

        return ResponseEntity.ok(buildResponse(
                response,
                "Candidate bookings fetched successfully",
                request.getRequestURI()
        ));
    }

    private <T> ApiResponse<T> buildResponse(
            T data,
            String message,
            String path) {
        // Default overload → statusCode = 200
        return buildResponse(data, message, path, 200);
    }

    private <T> ApiResponse<T> buildResponse(
            T data,
            String message,
            String path,
            Integer statusCode) {
        return ApiResponse.<T>builder()
                .success(true)
                .statusCode(statusCode != null ? statusCode : 200)
                .message(message)
                .data(data)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
}