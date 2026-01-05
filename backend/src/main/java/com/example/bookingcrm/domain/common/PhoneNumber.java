package com.example.bookingcrm.domain.common;

import java.util.Objects;

public record PhoneNumber(String value) {
    public PhoneNumber {
        Objects.requireNonNull(value, "value");
        String normalized = normalize(value);
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("phone must not be blank");
        }
        int digits = countDigits(normalized);
        if (digits < 7) {
            throw new IllegalArgumentException("phone is invalid");
        }
        value = normalized;
    }

    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
    }

    public static PhoneNumber ofNullable(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new PhoneNumber(value);
    }

    private static String normalize(String raw) {
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder(trimmed.length());
        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if (Character.isDigit(c) || c == '+') {
                result.append(c);
            }
        }
        return result.toString();
    }

    private static int countDigits(String value) {
        int count = 0;
        for (int i = 0; i < value.length(); i++) {
            if (Character.isDigit(value.charAt(i))) {
                count++;
            }
        }
        return count;
    }
}
