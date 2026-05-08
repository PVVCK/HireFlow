package com.hireflow.recruiter.dto.request;

import com.hireflow.common.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HiringDecisionRequest {

    @NotNull
    private Long candidateId;

    @NotNull
    private ApplicationStatus applicationStatus;
}
