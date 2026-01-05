package com.example.bookingcrm.infrastructure.persistence.jpa.repo;

import com.example.bookingcrm.infrastructure.persistence.jpa.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventJpaRepository extends JpaRepository<EventEntity, Long> {
    Optional<EventEntity> findByTenantIdAndId(long tenantId, long id);
}

