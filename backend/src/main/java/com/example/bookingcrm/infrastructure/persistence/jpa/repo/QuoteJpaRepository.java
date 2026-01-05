package com.example.bookingcrm.infrastructure.persistence.jpa.repo;

import com.example.bookingcrm.infrastructure.persistence.jpa.model.QuoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuoteJpaRepository extends JpaRepository<QuoteEntity, Long> {
    Optional<QuoteEntity> findByTenantIdAndId(long tenantId, long id);
}

