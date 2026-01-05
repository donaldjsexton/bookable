package com.example.bookingcrm.domain.common;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public record EmailAddress(String value) {
    private static final Pattern BASIC_EMAIL =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public EmailAddress {
        Objects.requireNonNull(value, "value");
        String normalized = value.trim().toLowerCase(Locale.ROOT);
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("email must not be blank");
        }
        if (!BASIC_EMAIL.matcher(normalized).matches()) {
            throw new IllegalArgumentException("email is invalid");
        }
        value = normalized;
    }

    public static EmailAddress of(String value) {
        return new EmailAddress(value);
    }

    public static EmailAddress ofNullable(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new EmailAddress(value);
    }
}
