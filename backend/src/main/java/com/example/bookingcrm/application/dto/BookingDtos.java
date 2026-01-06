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

    public record CreateBookingCommand(
            long tenantId,
            long clientId
    ) {}

    public record CreateWorkflowBookingCommand(
            long tenantId,
            String clientName,
            String eventType,
            java.time.LocalDate eventDate
    ) {}

    public record BookingWorkflowCandidateDto(
            long id,
            String label,
            String state,
            java.time.LocalDate eventDate
    ) {}

    public record WorkflowBookingActionResultDto(
            long bookingId,
            String label,
            String state
    ) {}
}
