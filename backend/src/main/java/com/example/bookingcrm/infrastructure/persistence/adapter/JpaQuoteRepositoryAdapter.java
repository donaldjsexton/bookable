package com.example.bookingcrm.infrastructure.persistence.adapter;

import com.example.bookingcrm.application.port.QuoteRepository;
import com.example.bookingcrm.domain.billing.Quote;
import com.example.bookingcrm.domain.billing.QuoteLineItem;
import com.example.bookingcrm.domain.billing.QuoteStatus;
import com.example.bookingcrm.infrastructure.persistence.jpa.model.QuoteEntity;
import com.example.bookingcrm.infrastructure.persistence.jpa.model.QuoteLineItemEntity;
import com.example.bookingcrm.infrastructure.persistence.jpa.repo.QuoteJpaRepository;
import com.example.bookingcrm.infrastructure.persistence.jpa.repo.QuoteLineItemJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaQuoteRepositoryAdapter implements QuoteRepository {
    private final QuoteJpaRepository quotes;
    private final QuoteLineItemJpaRepository lineItems;

    public JpaQuoteRepositoryAdapter(QuoteJpaRepository quotes, QuoteLineItemJpaRepository lineItems) {
        this.quotes = quotes;
        this.lineItems = lineItems;
    }

    @Override
    public Quote save(Quote quote) {
        QuoteEntity saved = quotes.save(toEntity(quote));
        return toDomain(saved);
    }

    @Override
    public Optional<Quote> findByTenantIdAndId(long tenantId, long quoteId) {
        return quotes.findByTenantIdAndId(tenantId, quoteId).map(JpaQuoteRepositoryAdapter::toDomain);
    }

    @Override
    public List<QuoteLineItem> saveLineItems(long quoteId, List<QuoteLineItem> items) {
        lineItems.deleteByQuoteId(quoteId);

        List<QuoteLineItemEntity> saved = lineItems.saveAll(items.stream()
                .map(item -> toEntity(quoteId, item))
                .toList());

        return saved.stream().map(JpaQuoteRepositoryAdapter::toDomain).toList();
    }

    @Override
    public List<QuoteLineItem> findLineItemsByQuoteId(long quoteId) {
        return lineItems.findAllByQuoteIdOrderBySortOrderAscIdAsc(quoteId).stream()
                .map(JpaQuoteRepositoryAdapter::toDomain)
                .toList();
    }

    private static QuoteEntity toEntity(Quote quote) {
        Long id = quote.id() == 0L ? null : quote.id();
        return new QuoteEntity(
                id,
                quote.tenantId(),
                quote.publicId(),
                quote.leadId(),
                quote.eventId(),
                quote.packageId(),
                quote.status().toString(),
                quote.validUntil()
        );
    }

    private static QuoteLineItemEntity toEntity(long quoteId, QuoteLineItem item) {
        Long id = item.id() == 0L ? null : item.id();
        return new QuoteLineItemEntity(
                id,
                quoteId,
                item.description(),
                item.quantity(),
                item.unitPrice(),
                item.sortOrder()
        );
    }

    private static Quote toDomain(QuoteEntity entity) {
        return new Quote(
                entity.getId(),
                entity.getTenantId(),
                entity.getPublicId(),
                entity.getLeadId(),
                entity.getEventId(),
                entity.getPackageId(),
                QuoteStatus.valueOf(entity.getStatus().trim().toUpperCase()),
                entity.getValidUntil()
        );
    }

    private static QuoteLineItem toDomain(QuoteLineItemEntity entity) {
        return new QuoteLineItem(
                entity.getId(),
                entity.getQuoteId(),
                entity.getDescription(),
                entity.getQuantity(),
                entity.getUnitPrice(),
                entity.getSortOrder()
        );
    }
}
