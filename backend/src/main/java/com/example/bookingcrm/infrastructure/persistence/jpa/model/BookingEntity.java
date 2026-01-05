package com.example.bookingcrm.infrastructure.persistence.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class BookingEntity extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private long tenantId;

    @Column(name = "client_id", nullable = false)
    private long clientId;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "quote_id")
    private Long quoteId;

    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "consult_date")
    private LocalDate consultDate;

    protected BookingEntity() {}

    public BookingEntity(
            Long id,
            long tenantId,
            long clientId,
            Long eventId,
            Long quoteId,
            Long invoiceId,
            String state,
            LocalDate consultDate
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.clientId = clientId;
        this.eventId = eventId;
        this.quoteId = quoteId;
        this.invoiceId = invoiceId;
        this.state = state;
        this.consultDate = consultDate;
    }

    public Long getId() {
        return id;
    }

    public long getTenantId() {
        return tenantId;
    }

    public long getClientId() {
        return clientId;
    }

    public Long getEventId() {
        return eventId;
    }

    public Long getQuoteId() {
        return quoteId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public String getState() {
        return state;
    }

    public LocalDate getConsultDate() {
        return consultDate;
    }
}
