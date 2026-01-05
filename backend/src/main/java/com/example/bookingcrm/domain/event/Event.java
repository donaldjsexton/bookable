package com.example.bookingcrm.domain.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

public record Event(
        long id,
        long tenantId,
        UUID publicId,
        long clientId,
        Long leadId,
        EventType type,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String locationName,
        String locationAddress,
        EventStatus status,
        Long galleryId,
        UUID galleryPublicId,
        String notes
) {
    public Event {
        if (tenantId <= 0) {
            throw new IllegalArgumentException("tenantId must be positive");
        }
        if (clientId <= 0) {
            throw new IllegalArgumentException("clientId must be positive");
        }
        if (leadId != null && leadId <= 0) {
            throw new IllegalArgumentException("leadId must be positive");
        }
        if (galleryId != null && galleryId <= 0) {
            throw new IllegalArgumentException("galleryId must be positive");
        }
        Objects.requireNonNull(publicId, "publicId");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(date, "date");
        Objects.requireNonNull(status, "status");
        validateTimeRange(startTime, endTime);
    }

    public static Event newFromLead(
            long tenantId,
            UUID publicId,
            long clientId,
            long leadId,
            EventType type,
            LocalDate date
    ) {
        return new Event(
                0L,
                tenantId,
                publicId,
                clientId,
                leadId,
                type,
                date,
                null,
                null,
                null,
                null,
                EventStatus.INQUIRY,
                null,
                null,
                null
        );
    }

    public Event withSchedule(LocalDate date, LocalTime startTime, LocalTime endTime) {
        return new Event(
                id,
                tenantId,
                publicId,
                clientId,
                leadId,
                type,
                Objects.requireNonNull(date, "date"),
                startTime,
                endTime,
                locationName,
                locationAddress,
                status,
                galleryId,
                galleryPublicId,
                notes
        );
    }

    public Event withLocation(String locationName, String locationAddress) {
        return new Event(
                id,
                tenantId,
                publicId,
                clientId,
                leadId,
                type,
                date,
                startTime,
                endTime,
                locationName,
                locationAddress,
                status,
                galleryId,
                galleryPublicId,
                notes
        );
    }

    public Event withStatus(EventStatus newStatus) {
        Objects.requireNonNull(newStatus, "newStatus");
        if (!status.canTransitionTo(newStatus)) {
            throw new IllegalArgumentException("invalid status transition: %s -> %s".formatted(status, newStatus));
        }
        return new Event(
                id,
                tenantId,
                publicId,
                clientId,
                leadId,
                type,
                date,
                startTime,
                endTime,
                locationName,
                locationAddress,
                newStatus,
                galleryId,
                galleryPublicId,
                notes
        );
    }

    private static void validateTimeRange(LocalTime start, LocalTime end) {
        if (end == null) {
            return;
        }
        if (start == null) {
            throw new IllegalArgumentException("startTime is required when endTime is set");
        }
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }
    }
}

