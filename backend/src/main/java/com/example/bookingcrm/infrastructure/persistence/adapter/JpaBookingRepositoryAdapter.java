package com.example.bookingcrm.infrastructure.persistence.adapter;

import com.example.bookingcrm.application.port.BookingRepository;
import com.example.bookingcrm.domain.booking.Booking;
import com.example.bookingcrm.domain.booking.BookingState;
import com.example.bookingcrm.infrastructure.persistence.jpa.model.BookingEntity;
import com.example.bookingcrm.infrastructure.persistence.jpa.repo.BookingJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaBookingRepositoryAdapter implements BookingRepository {
    private final BookingJpaRepository bookings;

    public JpaBookingRepositoryAdapter(BookingJpaRepository bookings) {
        this.bookings = bookings;
    }

    @Override
    public Booking save(Booking booking) {
        BookingEntity saved = bookings.save(toEntity(booking));
        return toDomain(saved);
    }

    @Override
    public Optional<Booking> findByTenantIdAndId(long tenantId, long bookingId) {
        return bookings.findByTenantIdAndId(tenantId, bookingId).map(JpaBookingRepositoryAdapter::toDomain);
    }

    @Override
    public List<Booking> findByTenantId(long tenantId) {
        return bookings.findByTenantIdOrderByCreatedAtDesc(tenantId).stream()
                .map(JpaBookingRepositoryAdapter::toDomain)
                .toList();
    }

    private static BookingEntity toEntity(Booking booking) {
        Long id = booking.id() == 0L ? null : booking.id();
        return new BookingEntity(
                id,
                booking.tenantId(),
                booking.clientId(),
                booking.eventId(),
                booking.quoteId(),
                booking.invoiceId(),
                booking.state().name(),
                booking.consultDate()
        );
    }

    private static Booking toDomain(BookingEntity entity) {
        return new Booking(
                entity.getId(),
                entity.getTenantId(),
                entity.getClientId(),
                entity.getEventId(),
                entity.getQuoteId(),
                entity.getInvoiceId(),
                BookingState.valueOf(entity.getState().trim().toUpperCase()),
                entity.getConsultDate()
        );
    }
}
