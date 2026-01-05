package com.example.bookingcrm.domain.automation;

public enum TriggerType {
    AFTER_EVENT,
    BEFORE_EVENT,
    AFTER_BOOKING,
    AFTER_PAYMENT;

    public static TriggerType fromText(String value) {
        if (value == null || value.isBlank()) {
            return AFTER_EVENT;
        }
        return TriggerType.valueOf(value.trim().toUpperCase());
    }
}

