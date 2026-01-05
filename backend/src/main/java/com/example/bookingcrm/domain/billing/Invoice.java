package com.example.bookingcrm.domain.billing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public record Invoice(
        long id,
        long tenantId,
        long eventId,
        UUID publicId,
        BigDecimal totalAmount,
        BigDecimal amountDue,
        LocalDate dueDate,
        InvoiceStatus status
) {
    public Invoice {
        if (tenantId <= 0) {
            throw new IllegalArgumentException("tenantId must be positive");
        }
        if (eventId <= 0) {
            throw new IllegalArgumentException("eventId must be positive");
        }
        Objects.requireNonNull(publicId, "publicId");
        totalAmount = money(totalAmount, "totalAmount");
        amountDue = money(amountDue, "amountDue");
        Objects.requireNonNull(status, "status");
    }

    public static Invoice newInvoice(long tenantId, long eventId, UUID publicId, BigDecimal totalAmount, LocalDate dueDate) {
        BigDecimal normalizedTotal = money(totalAmount, "totalAmount");
        return new Invoice(0L, tenantId, eventId, publicId, normalizedTotal, normalizedTotal, dueDate, InvoiceStatus.DRAFT);
    }

    public Invoice applyPayment(BigDecimal paymentAmount) {
        BigDecimal normalizedPayment = money(paymentAmount, "paymentAmount");
        if (normalizedPayment.signum() == 0) {
            return this;
        }

        BigDecimal newAmountDue = amountDue.subtract(normalizedPayment);
        if (newAmountDue.signum() < 0) {
            newAmountDue = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        InvoiceStatus newStatus;
        if (newAmountDue.signum() == 0) {
            newStatus = InvoiceStatus.PAID;
        } else if (newAmountDue.compareTo(totalAmount) < 0) {
            newStatus = InvoiceStatus.PARTIALLY_PAID;
        } else {
            newStatus = status;
        }

        return new Invoice(id, tenantId, eventId, publicId, totalAmount, newAmountDue, dueDate, newStatus);
    }

    private static BigDecimal money(BigDecimal value, String field) {
        Objects.requireNonNull(value, field);
        if (value.signum() < 0) {
            throw new IllegalArgumentException(field + " must be >= 0");
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
