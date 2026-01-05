package com.example.bookingcrm.infrastructure.persistence.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "leads")
public class LeadEntity extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private long tenantId;

    @Column(name = "client_id", nullable = false)
    private long clientId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "source")
    private String source;

    @Column(name = "estimated_budget")
    private BigDecimal estimatedBudget;

    @Column(name = "notes")
    private String notes;

    protected LeadEntity() {}

    public LeadEntity(
            Long id,
            long tenantId,
            long clientId,
            String status,
            String source,
            BigDecimal estimatedBudget,
            String notes
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.clientId = clientId;
        this.status = status;
        this.source = source;
        this.estimatedBudget = estimatedBudget;
        this.notes = notes;
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

    public String getStatus() {
        return status;
    }

    public String getSource() {
        return source;
    }

    public BigDecimal getEstimatedBudget() {
        return estimatedBudget;
    }

    public String getNotes() {
        return notes;
    }
}

