package com.example.bookingcrm.domain.lead;

public enum LeadStatus {
    NEW,
    CONTACTED,
    PROPOSAL_SENT,
    BOOKED,
    LOST;

    public static LeadStatus fromText(String value) {
        if (value == null || value.isBlank()) {
            return NEW;
        }
        String normalized = value.trim()
                .toUpperCase()
                .replace('-', '_')
                .replace(' ', '_');
        if (normalized.equals("QUALIFIED")) {
            return PROPOSAL_SENT;
        }
        if (normalized.equals("CONVERTED")) {
            return BOOKED;
        }
        return LeadStatus.valueOf(normalized);
    }

    public boolean canTransitionTo(LeadStatus next) {
        if (next == null) {
            return false;
        }
        if (this == next) {
            return true;
        }
        return switch (this) {
            case NEW -> next == CONTACTED || next == PROPOSAL_SENT || next == BOOKED || next == LOST;
            case CONTACTED -> next == PROPOSAL_SENT || next == BOOKED || next == LOST;
            case PROPOSAL_SENT -> next == BOOKED || next == LOST;
            case BOOKED, LOST -> false;
        };
    }
}
