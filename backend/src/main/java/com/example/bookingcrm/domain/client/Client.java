package com.example.bookingcrm.domain.client;

import java.util.Objects;
import java.util.UUID;

public record Client(
        long id,
        long tenantId,
        UUID publicId,
        String firstName,
        String lastName,
        String email,
        String phone,
        String preferredChannel,
        String notes
) {
    public Client {
        if (tenantId <= 0) {
            throw new IllegalArgumentException("tenantId must be positive");
        }
        Objects.requireNonNull(publicId, "publicId");
        requireText(firstName, "firstName");
        requireText(lastName, "lastName");
    }

    public static Client newClient(
            long tenantId,
            UUID publicId,
            String firstName,
            String lastName,
            String email,
            String phone,
            String preferredChannel,
            String notes
    ) {
        return new Client(0L, tenantId, publicId, firstName, lastName, email, phone, preferredChannel, notes);
    }

    public Client withProfile(
            String firstName,
            String lastName,
            String email,
            String phone,
            String preferredChannel,
            String notes
    ) {
        return new Client(id, tenantId, publicId, firstName, lastName, email, phone, preferredChannel, notes);
    }

    private static void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
    }
}

