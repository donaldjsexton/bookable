package com.example.bookingcrm.infrastructure.persistence.jpa.repo;

import com.example.bookingcrm.infrastructure.persistence.jpa.model.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientJpaRepository extends JpaRepository<ClientEntity, Long> {
    Optional<ClientEntity> findByTenantIdAndId(long tenantId, long id);
}

