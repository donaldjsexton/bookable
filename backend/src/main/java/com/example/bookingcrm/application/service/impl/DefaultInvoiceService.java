package com.example.bookingcrm.application.service.impl;

import com.example.bookingcrm.application.common.EntityNotFoundException;
import com.example.bookingcrm.application.dto.InvoiceDtos.CreateInvoiceCommand;
import com.example.bookingcrm.application.dto.InvoiceDtos.InvoiceDto;
import com.example.bookingcrm.application.dto.InvoiceDtos.PaymentDto;
import com.example.bookingcrm.application.dto.InvoiceDtos.RegisterPaymentCommand;
import com.example.bookingcrm.application.mapper.InvoiceMapper;
import com.example.bookingcrm.application.port.InvoiceRepository;
import com.example.bookingcrm.application.port.PaymentRepository;
import com.example.bookingcrm.application.service.InvoiceService;
import com.example.bookingcrm.domain.billing.Invoice;
import com.example.bookingcrm.domain.billing.Payment;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

public class DefaultInvoiceService implements InvoiceService {
    private final InvoiceRepository invoices;
    private final PaymentRepository payments;

    public DefaultInvoiceService(InvoiceRepository invoices, PaymentRepository payments) {
        this.invoices = invoices;
        this.payments = payments;
    }

    @Override
    public InvoiceDto create(CreateInvoiceCommand command) {
        Invoice created = invoices.save(Invoice.newInvoice(
                command.tenantId(),
                command.eventId(),
                UUID.randomUUID(),
                command.totalAmount(),
                command.dueDate()
        ));
        return InvoiceMapper.toDto(created);
    }

    @Override
    @Transactional
    public PaymentDto registerPayment(RegisterPaymentCommand command) {
        Invoice invoice = invoices.findByTenantIdAndId(command.tenantId(), command.invoiceId())
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found: tenantId=%s invoiceId=%s"
                        .formatted(command.tenantId(), command.invoiceId())));

        Instant receivedAt = command.receivedAt() == null ? Instant.now() : command.receivedAt();
        Payment payment = payments.save(Payment.newPayment(invoice.id(), command.amount(), command.method(), receivedAt, command.notes()));

        Invoice updatedInvoice = invoices.save(invoice.applyPayment(payment.amount()));

        return InvoiceMapper.toDto(payment.withInvoiceId(updatedInvoice.id()));
    }
}
