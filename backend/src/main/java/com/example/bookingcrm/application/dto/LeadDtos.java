package com.example.bookingcrm.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class LeadDtos {
    private LeadDtos() {}

    public record LeadDto(
            long id,
            long tenantId,
            long clientId,
            String status,
            String source,
            BigDecimal estimatedBudget,
            String notes
    ) {}

    public record CreateLeadCommand(
            long tenantId,
            long clientId,
            String status,
            String source,
            BigDecimal estimatedBudget,
            String notes
    ) {}

    public record UpdateLeadCommand(
            long tenantId,
            long leadId,
            String status,
            String source,
            BigDecimal estimatedBudget,
            String notes
    ) {}

    public record ConvertLeadToEventCommand(
            long tenantId,
            long leadId,
            String eventType,
            LocalDate eventDate
    ) {}
}
