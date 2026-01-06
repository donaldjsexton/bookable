package com.example.bookingcrm.application.port;

import com.example.bookingcrm.domain.client.Client;

import java.util.Optional;

public interface ClientRepository {
    Client save(Client client);

    Optional<Client> findByTenantIdAndId(long tenantId, long clientId);

    Optional<Client> findByTenantIdAndName(long tenantId, String firstName, String lastName);
}
