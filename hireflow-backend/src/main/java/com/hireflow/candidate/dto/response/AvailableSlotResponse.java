package com.hireflow.candidate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableSlotResponse {

    private Long slotId;
    private String interviewerName;
    private String interviewDate;
    private String startTime;
    private String endTime;
    private String slotStatus;
}
