package com.hireflow.interview.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateSlotRequest {

    @NotNull
    private Long interviewerId;

    @NotNull
    private LocalDate interviewDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;
}