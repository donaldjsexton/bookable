package com.example.bookingcrm.domain.billing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record QuoteLineItem(
        long id,
        long quoteId,
        String description,
        int quantity,
        BigDecimal unitPrice,
        int sortOrder
) {
    public QuoteLineItem {
        if (quoteId <= 0) {
            throw new IllegalArgumentException("quoteId must be positive");
        }
        requireText(description, "description");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        unitPrice = money(unitPrice, "unitPrice");
    }

    public static QuoteLineItem newLineItem(long quoteId, String description, int quantity, BigDecimal unitPrice, int sortOrder) {
        return new QuoteLineItem(0L, quoteId, description, quantity, unitPrice, sortOrder);
    }

    private static BigDecimal money(BigDecimal value, String field) {
        Objects.requireNonNull(value, field);
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    private static void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
    }
}
