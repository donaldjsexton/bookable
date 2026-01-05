package com.example.bookingcrm.domain.model;

import java.util.Objects;

public final class DomainPreconditions {
    private DomainPreconditions() {}

    public static <T> T requireNonNull(T value, String field) {
        return Objects.requireNonNull(value, field);
    }

    public static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value;
    }

    public static long requirePositive(long value, String field) {
        if (value <= 0) {
            throw new IllegalArgumentException(field + " must be positive");
        }
        return value;
    }

    public static int requirePositive(int value, String field) {
        if (value <= 0) {
            throw new IllegalArgumentException(field + " must be positive");
        }
        return value;
    }

    public static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}

