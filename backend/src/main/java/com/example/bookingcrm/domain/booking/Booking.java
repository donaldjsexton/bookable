package com.example.bookingcrm.domain.booking;

import com.example.bookingcrm.domain.model.DomainPreconditions;

import java.time.LocalDate;

public record Booking(
        long id,
        long tenantId,
        long clientId,
        Long eventId,
        Long quoteId,
        Long invoiceId,
        BookingState state,
        LocalDate consultDate
) {
    public Booking {
        DomainPreconditions.requirePositive(tenantId, "tenantId");
        DomainPreconditions.requirePositive(clientId, "clientId");
        if (eventId != null) {
            DomainPreconditions.requirePositive(eventId, "eventId");
        }
        if (quoteId != null) {
            DomainPreconditions.requirePositive(quoteId, "quoteId");
        }
        if (invoiceId != null) {
            DomainPreconditions.requirePositive(invoiceId, "invoiceId");
        }
        DomainPreconditions.requireNonNull(state, "state");
    }

    public static Booking newBooking(long tenantId, long clientId) {
        return new Booking(0L, tenantId, clientId, null, null, null, BookingState.INQUIRY, null);
    }

    public Booking withState(BookingState newState) {
        return new Booking(id, tenantId, clientId, eventId, quoteId, invoiceId, newState, consultDate);
    }
}
