package com.example.bookingcrm.infrastructure.persistence.adapter;

import com.example.bookingcrm.application.port.EventRepository;
import com.example.bookingcrm.domain.event.Event;
import com.example.bookingcrm.domain.event.EventStatus;
import com.example.bookingcrm.domain.event.EventType;
import com.example.bookingcrm.infrastructure.persistence.jpa.model.EventEntity;
import com.example.bookingcrm.infrastructure.persistence.jpa.repo.EventJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaEventRepositoryAdapter implements EventRepository {
    private final EventJpaRepository events;

    public JpaEventRepositoryAdapter(EventJpaRepository events) {
        this.events = events;
    }

    @Override
    public Event save(Event event) {
        EventEntity saved = events.save(toEntity(event));
        return toDomain(saved);
    }

    @Override
    public Optional<Event> findByTenantIdAndId(long tenantId, long eventId) {
        return events.findByTenantIdAndId(tenantId, eventId).map(JpaEventRepositoryAdapter::toDomain);
    }

    private static EventEntity toEntity(Event event) {
        Long id = event.id() == 0L ? null : event.id();
        return new EventEntity(
                id,
                event.tenantId(),
                event.publicId(),
                event.clientId(),
                event.leadId(),
                event.type().toString(),
                event.date(),
                event.startTime(),
                event.endTime(),
                event.locationName(),
                event.locationAddress(),
                event.status().toString(),
                event.galleryId(),
                event.galleryPublicId(),
                event.notes()
        );
    }

    private static Event toDomain(EventEntity entity) {
        return new Event(
                entity.getId(),
                entity.getTenantId(),
                entity.getPublicId(),
                entity.getClientId(),
                entity.getLeadId(),
                EventType.fromText(entity.getType()),
                entity.getDate(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getLocationName(),
                entity.getLocationAddress(),
                EventStatus.fromText(entity.getStatus()),
                entity.getGalleryId(),
                entity.getGalleryPublicId(),
                entity.getNotes()
        );
    }
}

