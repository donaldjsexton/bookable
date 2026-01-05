package com.example.bookingcrm.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public final class InvoiceDtos {
    private InvoiceDtos() {}

    public record InvoiceDto(
            long id,
            long tenantId,
            long eventId,
            UUID publicId,
            BigDecimal totalAmount,
            BigDecimal amountDue,
            LocalDate dueDate,
            String status
    ) {}

    public record CreateInvoiceCommand(
            long tenantId,
            long eventId,
            BigDecimal totalAmount,
            LocalDate dueDate
    ) {}

    public record PaymentDto(
            long id,
            long invoiceId,
            BigDecimal amount,
            String method,
            Instant receivedAt,
            String notes
    ) {}

    public record RegisterPaymentCommand(
            long tenantId,
            long invoiceId,
            BigDecimal amount,
            String method,
            Instant receivedAt,
            String notes
    ) {}
}

