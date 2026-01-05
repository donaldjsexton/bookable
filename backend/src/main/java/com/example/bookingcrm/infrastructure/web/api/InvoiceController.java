package com.example.bookingcrm.infrastructure.web.api;

import com.example.bookingcrm.application.dto.InvoiceDtos.CreateInvoiceCommand;
import com.example.bookingcrm.application.dto.InvoiceDtos.InvoiceDto;
import com.example.bookingcrm.application.dto.InvoiceDtos.PaymentDto;
import com.example.bookingcrm.application.dto.InvoiceDtos.RegisterPaymentCommand;
import com.example.bookingcrm.application.service.InvoiceService;
import com.example.bookingcrm.infrastructure.web.tenant.TenantContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private final InvoiceService invoices;

    public InvoiceController(InvoiceService invoices) {
        this.invoices = invoices;
    }

    @PostMapping
    public InvoiceDto create(@Valid @RequestBody CreateInvoiceRequest request) {
        return invoices.create(new CreateInvoiceCommand(
                TenantContext.requireTenantId(),
                request.eventId(),
                request.totalAmount(),
                request.dueDate()
        ));
    }

    @PostMapping("/{invoiceId}/payments")
    public PaymentDto registerPayment(@PathVariable long invoiceId, @Valid @RequestBody RegisterPaymentRequest request) {
        return invoices.registerPayment(new RegisterPaymentCommand(
                TenantContext.requireTenantId(),
                invoiceId,
                request.amount(),
                request.method(),
                request.receivedAt(),
                request.notes()
        ));
    }

    public record CreateInvoiceRequest(
            @Positive long eventId,
            @NotNull BigDecimal totalAmount,
            LocalDate dueDate
    ) {}

    public record RegisterPaymentRequest(
            @NotNull BigDecimal amount,
            @NotBlank String method,
            Instant receivedAt,
            String notes
    ) {}
}

