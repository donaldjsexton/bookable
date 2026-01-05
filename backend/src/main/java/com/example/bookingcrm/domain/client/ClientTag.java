package com.example.bookingcrm.domain.client;

import java.util.regex.Pattern;

public record ClientTag(
        long id,
        long tenantId,
        String name,
        String color,
        String description
) {
    private static final Pattern HEX_COLOR = Pattern.compile("^#(?:[0-9a-fA-F]{3}|[0-9a-fA-F]{6})$");

    public ClientTag {
        if (tenantId <= 0) {
            throw new IllegalArgumentException("tenantId must be positive");
        }
        requireText(name, "name");
        color = normalizeColor(color);
    }

    public static ClientTag newTag(long tenantId, String name, String color, String description) {
        return new ClientTag(0L, tenantId, name, color, description);
    }

    public ClientTag withDetails(String name, String color, String description) {
        return new ClientTag(id, tenantId, name, color, description);
    }

    private static void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
    }

    private static String normalizeColor(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        if (!HEX_COLOR.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("color must be a hex value like #RRGGBB");
        }
        return trimmed.toUpperCase();
    }
}
