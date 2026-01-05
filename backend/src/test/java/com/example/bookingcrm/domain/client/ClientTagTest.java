package com.example.bookingcrm.domain.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClientTagTest {

    @Test
    void normalizesColorToUppercase() {
        ClientTag tag = ClientTag.newTag(1L, "VIP", "#a1b2c3", null);
        assertEquals("#A1B2C3", tag.color());
    }

    @Test
    void treatsBlankColorAsNull() {
        ClientTag tag = ClientTag.newTag(1L, "VIP", "   ", null);
        assertNull(tag.color());
    }

    @Test
    void rejectsInvalidColor() {
        assertThrows(IllegalArgumentException.class, () -> ClientTag.newTag(1L, "VIP", "red", null));
    }
}

