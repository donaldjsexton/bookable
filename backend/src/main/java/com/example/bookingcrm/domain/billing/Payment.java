package com.example.bookingcrm.domain.billing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;

public record Payment(
        long id,
        long invoiceId,
        BigDecimal amount,
        String method,
        Instant receivedAt,
        String notes
) {
    public Payment {
        if (invoiceId <= 0) {
            throw new IllegalArgumentException("invoiceId must be positive");
        }
        amount = money(amount, "amount");
        Objects.requireNonNull(method, "method");
        Objects.requireNonNull(receivedAt, "receivedAt");
    }

    public static Payment newPayment(long invoiceId, BigDecimal amount, String method, Instant receivedAt, String notes) {
        return new Payment(0L, invoiceId, amount, method, receivedAt, notes);
    }

    public Payment withInvoiceId(long invoiceId) {
        return new Payment(id, invoiceId, amount, method, receivedAt, notes);
    }

    private static BigDecimal money(BigDecimal value, String field) {
        Objects.requireNonNull(value, field);
        if (value.signum() < 0) {
            throw new IllegalArgumentException(field + " must be >= 0");
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
