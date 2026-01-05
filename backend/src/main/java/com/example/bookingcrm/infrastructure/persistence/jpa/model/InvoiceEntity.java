package com.example.bookingcrm.infrastructure.persistence.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "invoices")
public class InvoiceEntity extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private long tenantId;

    @Column(name = "event_id", nullable = false)
    private long eventId;

    @Column(name = "public_id", nullable = false)
    private UUID publicId;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "amount_due", nullable = false)
    private BigDecimal amountDue;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "status", nullable = false)
    private String status;

    protected InvoiceEntity() {}

    public InvoiceEntity(
            Long id,
            long tenantId,
            long eventId,
            UUID publicId,
            BigDecimal totalAmount,
            BigDecimal amountDue,
            LocalDate dueDate,
            String status
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.eventId = eventId;
        this.publicId = publicId;
        this.totalAmount = totalAmount;
        this.amountDue = amountDue;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public long getTenantId() {
        return tenantId;
    }

    public long getEventId() {
        return eventId;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }
}

