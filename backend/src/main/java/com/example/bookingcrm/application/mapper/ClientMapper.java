package com.example.bookingcrm.application.mapper;

import com.example.bookingcrm.application.dto.ClientDtos.ClientDto;
import com.example.bookingcrm.domain.client.Client;

public final class ClientMapper {
    private ClientMapper() {}

    public static ClientDto toDto(Client client) {
        return new ClientDto(
                client.id(),
                client.tenantId(),
                client.publicId(),
                client.firstName(),
                client.lastName(),
                client.email(),
                client.phone(),
                client.preferredChannel(),
                client.notes()
        );
    }
}
