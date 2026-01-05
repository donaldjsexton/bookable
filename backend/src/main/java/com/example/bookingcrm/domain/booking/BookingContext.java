package com.example.bookingcrm.domain.booking;

public record BookingContext(
        boolean hasClientEmail,
        boolean hasConsultDate,
        boolean hasQuote,
        boolean proposalSigned,
        boolean depositRecorded,
        boolean eventDatePassed,
        boolean imagesUploaded,
        boolean finalPaymentRecorded
) {}
