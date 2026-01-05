package com.example.bookingcrm.domain.model;

import java.util.Objects;
import java.util.UUID;

public record PublicId(UUID value) {
    public PublicId {
        Objects.requireNonNull(value, "value");
    }

    public static PublicId generate() {
        return new PublicId(UUID.randomUUID());
    }

    public static PublicId of(UUID value) {
        return new PublicId(value);
    }
}

