package com.example.bookingcrm.application.port;

import com.example.bookingcrm.domain.event.Event;

import java.util.Optional;

public interface EventRepository {
    Event save(Event event);

    Optional<Event> findByTenantIdAndId(long tenantId, long eventId);
}
