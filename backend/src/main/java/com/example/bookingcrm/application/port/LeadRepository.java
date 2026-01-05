package com.example.bookingcrm.application.port;

import com.example.bookingcrm.domain.lead.Lead;

import java.util.Optional;

public interface LeadRepository {
    Lead save(Lead lead);

    Optional<Lead> findByTenantIdAndId(long tenantId, long leadId);
}
