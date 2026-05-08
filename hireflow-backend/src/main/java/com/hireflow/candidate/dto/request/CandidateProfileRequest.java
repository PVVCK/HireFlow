package com.hireflow.candidate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CandidateProfileRequest {

    @NotBlank
    private String skills;

    @NotNull
    private Integer experienceYears;

    private String resumeUrl;
}
