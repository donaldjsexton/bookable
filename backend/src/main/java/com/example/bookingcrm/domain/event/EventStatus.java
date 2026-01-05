package com.example.bookingcrm.domain.event;

public enum EventStatus {
    INQUIRY,
    PENCILED,
    BOOKED,
    COMPLETED,
    CANCELLED;

    public static EventStatus fromText(String value) {
        if (value == null || value.isBlank()) {
            return INQUIRY;
        }
        return EventStatus.valueOf(value.trim().toUpperCase());
    }

    public boolean canTransitionTo(EventStatus next) {
        if (next == null) {
            return false;
        }
        if (this == next) {
            return true;
        }
        return switch (this) {
            case INQUIRY -> next == PENCILED || next == BOOKED || next == CANCELLED;
            case PENCILED -> next == BOOKED || next == CANCELLED;
            case BOOKED -> next == COMPLETED || next == CANCELLED;
            case COMPLETED, CANCELLED -> false;
        };
    }
}
