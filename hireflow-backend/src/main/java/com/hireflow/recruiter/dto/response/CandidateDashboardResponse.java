package com.hireflow.recruiter.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidateDashboardResponse {

    private Long candidateId;
    private String candidateName;
    private String email;
    private String applicationStatus;
    private String rejectionReason;
    private String skills;
    private Integer experienceYears;
}
