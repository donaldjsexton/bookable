package com.example.bookingcrm.infrastructure.persistence.adapter;

import com.example.bookingcrm.application.port.InvoiceRepository;
import com.example.bookingcrm.domain.billing.Invoice;
import com.example.bookingcrm.domain.billing.InvoiceStatus;
import com.example.bookingcrm.infrastructure.persistence.jpa.model.InvoiceEntity;
import com.example.bookingcrm.infrastructure.persistence.jpa.repo.InvoiceJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaInvoiceRepositoryAdapter implements InvoiceRepository {
    private final InvoiceJpaRepository invoices;

    public JpaInvoiceRepositoryAdapter(InvoiceJpaRepository invoices) {
        this.invoices = invoices;
    }

    @Override
    public Invoice save(Invoice invoice) {
        InvoiceEntity saved = invoices.save(toEntity(invoice));
        return toDomain(saved);
    }

    @Override
    public Optional<Invoice> findByTenantIdAndId(long tenantId, long invoiceId) {
        return invoices.findByTenantIdAndId(tenantId, invoiceId).map(JpaInvoiceRepositoryAdapter::toDomain);
    }

    private static InvoiceEntity toEntity(Invoice invoice) {
        Long id = invoice.id() == 0L ? null : invoice.id();
        return new InvoiceEntity(
                id,
                invoice.tenantId(),
                invoice.eventId(),
                invoice.publicId(),
                invoice.totalAmount(),
                invoice.amountDue(),
                invoice.dueDate(),
                invoice.status().toString()
        );
    }

    private static Invoice toDomain(InvoiceEntity entity) {
        return new Invoice(
                entity.getId(),
                entity.getTenantId(),
                entity.getEventId(),
                entity.getPublicId(),
                entity.getTotalAmount(),
                entity.getAmountDue(),
                entity.getDueDate(),
                InvoiceStatus.valueOf(entity.getStatus().trim().toUpperCase())
        );
    }
}
