package com.example.bookingcrm.domain.event;

public enum EventType {
    WEDDING,
    ELOPEMENT,
    SESSION;

    public static EventType fromText(String value) {
        if (value == null || value.isBlank()) {
            return SESSION;
        }
        String normalized = value.trim()
                .toUpperCase()
                .replace('-', '_')
                .replace(' ', '_');
        return EventType.valueOf(normalized);
    }
}

