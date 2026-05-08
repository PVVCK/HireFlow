package com.hireflow.candidate.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {

    @NotNull
    private Long candidateId;

    @NotNull
    private Long slotId;
}
