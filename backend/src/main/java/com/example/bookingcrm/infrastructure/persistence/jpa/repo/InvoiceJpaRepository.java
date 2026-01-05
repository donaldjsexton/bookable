package com.example.bookingcrm.infrastructure.persistence.jpa.repo;

import com.example.bookingcrm.infrastructure.persistence.jpa.model.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, Long> {
    Optional<InvoiceEntity> findByTenantIdAndId(long tenantId, long id);
}

