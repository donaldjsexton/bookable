package com.example.bookingcrm.infrastructure.persistence.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "quotes")
public class QuoteEntity extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private long tenantId;

    @Column(name = "public_id", nullable = false)
    private UUID publicId;

    @Column(name = "lead_id")
    private Long leadId;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "package_id")
    private Long packageId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "valid_until")
    private LocalDate validUntil;

    protected QuoteEntity() {}

    public QuoteEntity(
            Long id,
            long tenantId,
            UUID publicId,
            Long leadId,
            Long eventId,
            Long packageId,
            String status,
            LocalDate validUntil
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.publicId = publicId;
        this.leadId = leadId;
        this.eventId = eventId;
        this.packageId = packageId;
        this.status = status;
        this.validUntil = validUntil;
    }

    public Long getId() {
        return id;
    }

    public long getTenantId() {
        return tenantId;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public Long getLeadId() {
        return leadId;
    }

    public Long getEventId() {
        return eventId;
    }

    public Long getPackageId() {
        return packageId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }
}

