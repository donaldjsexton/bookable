package com.example.bookingcrm.domain.billing;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public record Quote(
        long id,
        long tenantId,
        UUID publicId,
        Long leadId,
        Long eventId,
        Long packageId,
        QuoteStatus status,
        LocalDate validUntil
) {
    public Quote {
        if (tenantId <= 0) {
            throw new IllegalArgumentException("tenantId must be positive");
        }
        Objects.requireNonNull(publicId, "publicId");
        Objects.requireNonNull(status, "status");
    }

    public static Quote newQuote(
            long tenantId,
            UUID publicId,
            Long leadId,
            Long eventId,
            Long packageId,
            QuoteStatus status,
            LocalDate validUntil
    ) {
        return new Quote(0L, tenantId, publicId, leadId, eventId, packageId, status, validUntil);
    }

    public Quote withStatus(QuoteStatus status) {
        return new Quote(id, tenantId, publicId, leadId, eventId, packageId, status, validUntil);
    }
}
