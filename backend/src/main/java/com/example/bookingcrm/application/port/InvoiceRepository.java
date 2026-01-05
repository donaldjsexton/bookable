package com.example.bookingcrm.application.port;

import com.example.bookingcrm.domain.billing.Invoice;

import java.util.Optional;

public interface InvoiceRepository {
    Invoice save(Invoice invoice);

    Optional<Invoice> findByTenantIdAndId(long tenantId, long invoiceId);
}
