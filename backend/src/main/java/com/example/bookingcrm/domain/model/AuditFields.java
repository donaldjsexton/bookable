package com.example.bookingcrm.domain.model;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

public record AuditFields(Instant createdAt, Instant updatedAt) {
    public AuditFields {
        Objects.requireNonNull(createdAt, "createdAt");
        Objects.requireNonNull(updatedAt, "updatedAt");
        if (updatedAt.isBefore(createdAt)) {
            throw new IllegalArgumentException("updatedAt must be >= createdAt");
        }
    }

    public static AuditFields createdNow(Clock clock) {
        Objects.requireNonNull(clock, "clock");
        Instant now = Instant.now(clock);
        return new AuditFields(now, now);
    }

    public AuditFields touch(Clock clock) {
        Objects.requireNonNull(clock, "clock");
        return new AuditFields(createdAt, Instant.now(clock));
    }
}

