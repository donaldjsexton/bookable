package com.example.bookingcrm.infrastructure.persistence.jpa.repo;

import com.example.bookingcrm.infrastructure.persistence.jpa.model.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingJpaRepository extends JpaRepository<BookingEntity, Long> {
    Optional<BookingEntity> findByTenantIdAndId(long tenantId, long id);

    List<BookingEntity> findByTenantIdOrderByCreatedAtDesc(long tenantId);
}
