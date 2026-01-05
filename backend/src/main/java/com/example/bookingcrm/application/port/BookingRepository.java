package com.example.bookingcrm.application.port;

import com.example.bookingcrm.domain.booking.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Booking save(Booking booking);

    Optional<Booking> findByTenantIdAndId(long tenantId, long bookingId);

    List<Booking> findByTenantId(long tenantId);
}
