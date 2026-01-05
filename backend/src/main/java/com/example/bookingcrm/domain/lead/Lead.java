package com.example.bookingcrm.domain.lead;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Lead(
        long id,
        long tenantId,
        long clientId,
        LeadStatus status,
        String source,
        BigDecimal estimatedBudget,
        String notes
) {
    public Lead {
        if (tenantId <= 0) {
            throw new IllegalArgumentException("tenantId must be positive");
        }
        if (clientId <= 0) {
            throw new IllegalArgumentException("clientId must be positive");
        }
        Objects.requireNonNull(status, "status");
        estimatedBudget = normalizeBudget(estimatedBudget);
    }

    public static Lead newLead(
            long tenantId,
            long clientId,
            LeadStatus status,
            String source,
            BigDecimal estimatedBudget,
            String notes
    ) {
        return new Lead(0L, tenantId, clientId, status, source, estimatedBudget, notes);
    }

    public Lead withDetails(LeadStatus status, String source, BigDecimal estimatedBudget, String notes) {
        return new Lead(id, tenantId, clientId, Objects.requireNonNull(status, "status"), source, estimatedBudget, notes);
    }

    public Lead withStatus(LeadStatus newStatus) {
        Objects.requireNonNull(newStatus, "newStatus");
        if (!status.canTransitionTo(newStatus)) {
            throw new IllegalArgumentException("invalid status transition: %s -> %s".formatted(status, newStatus));
        }
        return new Lead(id, tenantId, clientId, newStatus, source, estimatedBudget, notes);
    }

    public Lead markBooked() {
        return withStatus(LeadStatus.BOOKED);
    }

    public Lead markLost() {
        return withStatus(LeadStatus.LOST);
    }

    public Lead markConverted() {
        return markBooked();
    }

    private static BigDecimal normalizeBudget(BigDecimal value) {
        if (value == null) {
            return null;
        }
        if (value.signum() < 0) {
            throw new IllegalArgumentException("estimatedBudget must be >= 0");
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
