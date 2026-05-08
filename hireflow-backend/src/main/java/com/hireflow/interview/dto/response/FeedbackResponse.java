package com.hireflow.interview.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {

    private Long feedbackId;
    private Integer rating;
    private String comments;
    private String recommendationStatus;
}
