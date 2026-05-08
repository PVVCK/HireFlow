package com.hireflow.interview.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterviewBookingResponse {

    private Long bookingId;
    private String candidateName;
    private String bookingStatus;
    private String scheduledAt;
}