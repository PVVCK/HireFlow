package com.hireflow.interview.dto.request;

import com.hireflow.common.enums.RecommendationStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FeedbackRequest {

    @NotNull
    private Long bookingId;

    @Min(1)
    @Max(5)
    private Integer rating;

    private String comments;

    @NotNull
    private RecommendationStatus recommendationStatus;
}