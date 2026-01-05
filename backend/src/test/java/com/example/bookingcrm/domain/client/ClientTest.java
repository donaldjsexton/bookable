package com.example.bookingcrm.domain.client;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ClientTest {

    @Test
    void requiresPositiveTenantId() {
        assertThrows(IllegalArgumentException.class, () -> Client.newClient(
                0L,
                UUID.randomUUID(),
                "Ada",
                "Lovelace",
                null,
                null,
                null,
                null
        ));
    }
}

