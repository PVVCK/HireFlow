package com.hireflow.candidate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidateProfileResponse {

    private Long candidateId;
    private String name;
    private String email;
    private String skills;
    private Integer experienceYears;
    private String resumeUrl;
    private String applicationStatus;
}