package com.example.bookingcrm.application.mapper;

import com.example.bookingcrm.application.dto.LeadDtos.LeadDto;
import com.example.bookingcrm.domain.lead.Lead;

public final class LeadMapper {
    private LeadMapper() {}

    public static LeadDto toDto(Lead lead) {
        return new LeadDto(
                lead.id(),
                lead.tenantId(),
                lead.clientId(),
                lead.status().name(),
                lead.source(),
                lead.estimatedBudget(),
                lead.notes()
        );
    }
}
