package com.hireflow.interview.controller;

import com.hireflow.common.response.ApiResponse;
import com.hireflow.interview.dto.request.*;
import com.hireflow.interview.dto.response.*;
import com.hireflow.interview.service.InterviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("/slots")
    public ApiResponse<SlotResponse> createSlot(
            @Valid @RequestBody CreateSlotRequest request,
            HttpServletRequest httpRequest) {

        return buildResponse(
                interviewService.createSlot(request),
                "Slot created successfully",
                httpRequest.getRequestURI()
        );
    }

    @PutMapping("/slots/{slotId}")
    public ApiResponse<SlotResponse> updateSlot(
            @PathVariable Long slotId,
            @RequestBody UpdateSlotRequest request,
            HttpServletRequest httpRequest) {

        return buildResponse(
                interviewService.updateSlot(slotId, request),
                "Slot updated successfully",
                httpRequest.getRequestURI()
        );
    }

    @DeleteMapping("/slots/{slotId}")
    public ApiResponse<String> deleteSlot(
            @PathVariable Long slotId,
            HttpServletRequest request) {

        interviewService.deleteSlot(slotId);

        return buildResponse(
                "Slot deleted successfully",
                "Slot deleted successfully",
                request.getRequestURI()
        );
    }

    @GetMapping("/{interviewerId}/bookings")
    public ApiResponse<List<InterviewBookingResponse>> getBookings(
            @PathVariable Long interviewerId,
            HttpServletRequest request) {

        return buildResponse(
                interviewService.getScheduledBookings(interviewerId),
                "Bookings fetched successfully",
                request.getRequestURI()
        );
    }

    @PostMapping("/feedback")
    public ApiResponse<FeedbackResponse> submitFeedback(
            @Valid @RequestBody FeedbackRequest request,
            HttpServletRequest httpRequest) {

        return buildResponse(
                interviewService.submitFeedback(request),
                "Feedback submitted successfully",
                httpRequest.getRequestURI()
        );
    }

    @GetMapping("/{interviewerId}/slots")
    public ApiResponse<List<SlotResponse>> getInterviewerSlots(
            @PathVariable Long interviewerId,
            HttpServletRequest request) {

        return buildResponse(
                interviewService.getInterviewerSlots(interviewerId),
                "Slots fetched successfully",
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