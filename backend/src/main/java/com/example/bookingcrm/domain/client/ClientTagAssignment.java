package com.example.bookingcrm.domain.client;

public record ClientTagAssignment(long clientId, long tagId) {
    public ClientTagAssignment {
        if (clientId <= 0) {
            throw new IllegalArgumentException("clientId must be positive");
        }
        if (tagId <= 0) {
            throw new IllegalArgumentException("tagId must be positive");
        }
    }
}

