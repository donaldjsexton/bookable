package com.example.bookingcrm.domain.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount) {
    public Money {
        Objects.requireNonNull(amount, "amount");
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("amount must be >= 0");
        }
        amount = normalize(amount);
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    public Money plus(Money other) {
        Objects.requireNonNull(other, "other");
        return new Money(amount.add(other.amount));
    }

    public Money minus(Money other) {
        Objects.requireNonNull(other, "other");
        BigDecimal result = amount.subtract(other.amount);
        if (result.signum() < 0) {
            result = BigDecimal.ZERO;
        }
        return new Money(result);
    }

    private static BigDecimal normalize(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
