package com.hireflow.candidate.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RescheduleBookingRequest {

    @NotNull
    private Long bookingId;

    @NotNull
    private Long newSlotId;
}
