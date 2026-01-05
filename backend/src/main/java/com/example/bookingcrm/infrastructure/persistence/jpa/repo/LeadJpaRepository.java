package com.example.bookingcrm.infrastructure.persistence.jpa.repo;

import com.example.bookingcrm.infrastructure.persistence.jpa.model.LeadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeadJpaRepository extends JpaRepository<LeadEntity, Long> {
    Optional<LeadEntity> findByTenantIdAndId(long tenantId, long id);
}

