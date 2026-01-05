package com.example.bookingcrm.application.dto;

import java.util.UUID;

public final class ClientDtos {
    private ClientDtos() {}

    public record ClientDto(
            long id,
            long tenantId,
            UUID publicId,
            String firstName,
            String lastName,
            String email,
            String phone,
            String preferredChannel,
            String notes
    ) {}

    public record CreateClientCommand(
            long tenantId,
            String firstName,
            String lastName,
            String email,
            String phone,
            String preferredChannel,
            String notes
    ) {}

    public record UpdateClientCommand(
            long tenantId,
            long clientId,
            String firstName,
            String lastName,
            String email,
            String phone,
            String preferredChannel,
            String notes
    ) {}
}

