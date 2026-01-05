package com.example.bookingcrm.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class QuoteDtos {
    private QuoteDtos() {}

    public record QuoteDto(
            long id,
            long tenantId,
            UUID publicId,
            Long leadId,
            Long eventId,
            Long packageId,
            String status,
            LocalDate validUntil,
            List<QuoteLineItemDto> lineItems
    ) {}

    public record QuoteLineItemDto(
            long id,
            String description,
            int quantity,
            BigDecimal unitPrice,
            int sortOrder
    ) {}

    public record GenerateQuoteCommand(
            long tenantId,
            Long leadId,
            Long eventId,
            Long packageId,
            LocalDate validUntil,
            List<LineItem> lineItems
    ) {}

    public record LineItem(
            String description,
            int quantity,
            BigDecimal unitPrice,
            int sortOrder
    ) {}

    public record SendQuoteCommand(
            long tenantId,
            long quoteId,
            String recipientEmail
    ) {}
}

