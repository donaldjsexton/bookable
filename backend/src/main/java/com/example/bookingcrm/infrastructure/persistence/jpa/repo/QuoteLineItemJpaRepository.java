package com.example.bookingcrm.infrastructure.persistence.jpa.repo;

import com.example.bookingcrm.infrastructure.persistence.jpa.model.QuoteLineItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuoteLineItemJpaRepository extends JpaRepository<QuoteLineItemEntity, Long> {
    void deleteByQuoteId(long quoteId);

    List<QuoteLineItemEntity> findAllByQuoteIdOrderBySortOrderAscIdAsc(long quoteId);
}

