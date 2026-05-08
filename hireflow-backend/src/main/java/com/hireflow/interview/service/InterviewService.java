package com.hireflow.interview.service;

import com.hireflow.interview.dto.request.CreateSlotRequest;
import com.hireflow.interview.dto.request.FeedbackRequest;
import com.hireflow.interview.dto.request.UpdateSlotRequest;
import com.hireflow.interview.dto.response.FeedbackResponse;
import com.hireflow.interview.dto.response.InterviewBookingResponse;
import com.hireflow.interview.dto.response.SlotResponse;

import java.util.List;

public interface InterviewService {

    SlotResponse createSlot(CreateSlotRequest request);

    SlotResponse updateSlot(
            Long slotId,
            UpdateSlotRequest request
    );

    void deleteSlot(Long slotId);

    List<InterviewBookingResponse> getScheduledBookings(Long interviewerId);

    FeedbackResponse submitFeedback( FeedbackRequest request);

    List<SlotResponse> getInterviewerSlots(Long interviewerId);
}