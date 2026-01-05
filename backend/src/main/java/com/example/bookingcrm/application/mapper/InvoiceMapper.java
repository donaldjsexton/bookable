package com.example.bookingcrm.application.mapper;

import com.example.bookingcrm.application.dto.InvoiceDtos.InvoiceDto;
import com.example.bookingcrm.application.dto.InvoiceDtos.PaymentDto;
import com.example.bookingcrm.domain.billing.Invoice;
import com.example.bookingcrm.domain.billing.Payment;

public final class InvoiceMapper {
    private InvoiceMapper() {}

    public static InvoiceDto toDto(Invoice invoice) {
        return new InvoiceDto(
                invoice.id(),
                invoice.tenantId(),
                invoice.eventId(),
                invoice.publicId(),
                invoice.totalAmount(),
                invoice.amountDue(),
                invoice.dueDate(),
                invoice.status().name()
        );
    }

    public static PaymentDto toDto(Payment payment) {
        return new PaymentDto(
                payment.id(),
                payment.invoiceId(),
                payment.amount(),
                payment.method(),
                payment.receivedAt(),
                payment.notes()
        );
    }
}
