package com.example.bookingcrm.application.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public final class EventDtos {
    private EventDtos() {}

    public record EventDto(
            long id,
            long tenantId,
            UUID publicId,
            long clientId,
            Long leadId,
            String type,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            String locationName,
            String locationAddress,
            String status,
            Long galleryId,
            UUID galleryPublicId,
            String notes
    ) {}
}

