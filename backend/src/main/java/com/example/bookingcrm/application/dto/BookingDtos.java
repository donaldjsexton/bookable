package com.example.bookingcrm.application.dto;

import java.util.List;

public final class BookingDtos {
    private BookingDtos() {}

    public record BookingStateDto(
            String state,
            List<String> allowedActions
    ) {}

    public record BookingStateQuery(
            long tenantId,
            long bookingId
    ) {}

    public record BookingWorkflowCandidatesQuery(
            long tenantId
    ) {}

    public record BookingWorkflowCandidateDto(
            long id,
            String label,
            String state
    ) {}
}
