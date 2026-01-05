package com.example.bookingcrm.application.mapper;

import com.example.bookingcrm.application.dto.EventDtos.EventDto;
import com.example.bookingcrm.domain.event.Event;

public final class EventMapper {
    private EventMapper() {}

    public static EventDto toDto(Event event) {
        return new EventDto(
                event.id(),
                event.tenantId(),
                event.publicId(),
                event.clientId(),
                event.leadId(),
                event.type().name(),
                event.date(),
                event.startTime(),
                event.endTime(),
                event.locationName(),
                event.locationAddress(),
                event.status().name(),
                event.galleryId(),
                event.galleryPublicId(),
                event.notes()
        );
    }
}
