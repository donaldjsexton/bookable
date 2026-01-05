package com.example.bookingcrm.infrastructure.persistence.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "quote_line_items")
public class QuoteLineItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quote_id", nullable = false)
    private long quoteId;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    protected QuoteLineItemEntity() {}

    public QuoteLineItemEntity(Long id, long quoteId, String description, int quantity, BigDecimal unitPrice, int sortOrder) {
        this.id = id;
        this.quoteId = quoteId;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.sortOrder = sortOrder;
    }

    public Long getId() {
        return id;
    }

    public long getQuoteId() {
        return quoteId;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}

