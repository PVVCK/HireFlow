package com.hireflow.recruiter.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecruiterDashboardResponse {

    private Long totalCandidates;
    private Long totalScheduledInterviews;
    private Long selectedCandidates;
    private Long rejectedCandidates;
}