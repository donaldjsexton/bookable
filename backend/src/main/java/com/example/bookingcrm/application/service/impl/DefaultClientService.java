package com.example.bookingcrm.application.service.impl;

import com.example.bookingcrm.application.common.EntityNotFoundException;
import com.example.bookingcrm.application.dto.ClientDtos.ClientDto;
import com.example.bookingcrm.application.dto.ClientDtos.CreateClientCommand;
import com.example.bookingcrm.application.dto.ClientDtos.UpdateClientCommand;
import com.example.bookingcrm.application.mapper.ClientMapper;
import com.example.bookingcrm.application.port.ClientRepository;
import com.example.bookingcrm.application.service.ClientService;
import com.example.bookingcrm.domain.client.Client;

import java.util.UUID;

public class DefaultClientService implements ClientService {
    private final ClientRepository clients;

    public DefaultClientService(ClientRepository clients) {
        this.clients = clients;
    }

    @Override
    public ClientDto create(CreateClientCommand command) {
        Client created = clients.save(Client.newClient(
                command.tenantId(),
                UUID.randomUUID(),
                command.firstName(),
                command.lastName(),
                command.email(),
                command.phone(),
                command.preferredChannel(),
                command.notes()
        ));
        return ClientMapper.toDto(created);
    }

    @Override
    public ClientDto update(UpdateClientCommand command) {
        Client existing = clients.findByTenantIdAndId(command.tenantId(), command.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found: tenantId=%s clientId=%s"
                        .formatted(command.tenantId(), command.clientId())));

        Client updated = clients.save(existing.withProfile(
                command.firstName(),
                command.lastName(),
                command.email(),
                command.phone(),
                command.preferredChannel(),
                command.notes()
        ));
        return ClientMapper.toDto(updated);
    }
}
