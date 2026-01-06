package com.example.bookingcrm.infrastructure.persistence.adapter;

import com.example.bookingcrm.application.port.ClientRepository;
import com.example.bookingcrm.domain.client.Client;
import com.example.bookingcrm.infrastructure.persistence.jpa.model.ClientEntity;
import com.example.bookingcrm.infrastructure.persistence.jpa.repo.ClientJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaClientRepositoryAdapter implements ClientRepository {
    private final ClientJpaRepository clients;

    public JpaClientRepositoryAdapter(ClientJpaRepository clients) {
        this.clients = clients;
    }

    @Override
    public Client save(Client client) {
        ClientEntity saved = clients.save(toEntity(client));
        return toDomain(saved);
    }

    @Override
    public Optional<Client> findByTenantIdAndId(long tenantId, long clientId) {
        return clients.findByTenantIdAndId(tenantId, clientId).map(JpaClientRepositoryAdapter::toDomain);
    }

    @Override
    public Optional<Client> findByTenantIdAndName(long tenantId, String firstName, String lastName) {
        return clients.findByTenantIdAndFirstNameIgnoreCaseAndLastNameIgnoreCase(tenantId, firstName, lastName)
                .map(JpaClientRepositoryAdapter::toDomain);
    }

    private static ClientEntity toEntity(Client client) {
        Long id = client.id() == 0L ? null : client.id();
        return new ClientEntity(
                id,
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

    private static Client toDomain(ClientEntity entity) {
        return new Client(
                entity.getId(),
                entity.getTenantId(),
                entity.getPublicId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getPreferredChannel(),
                entity.getNotes()
        );
    }
}
