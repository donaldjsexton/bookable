package com.example.bookingcrm.infrastructure.persistence.adapter;

import com.example.bookingcrm.application.port.LeadRepository;
import com.example.bookingcrm.domain.lead.Lead;
import com.example.bookingcrm.domain.lead.LeadStatus;
import com.example.bookingcrm.infrastructure.persistence.jpa.model.LeadEntity;
import com.example.bookingcrm.infrastructure.persistence.jpa.repo.LeadJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaLeadRepositoryAdapter implements LeadRepository {
    private final LeadJpaRepository leads;

    public JpaLeadRepositoryAdapter(LeadJpaRepository leads) {
        this.leads = leads;
    }

    @Override
    public Lead save(Lead lead) {
        LeadEntity saved = leads.save(toEntity(lead));
        return toDomain(saved);
    }

    @Override
    public Optional<Lead> findByTenantIdAndId(long tenantId, long leadId) {
        return leads.findByTenantIdAndId(tenantId, leadId).map(JpaLeadRepositoryAdapter::toDomain);
    }

    private static LeadEntity toEntity(Lead lead) {
        Long id = lead.id() == 0L ? null : lead.id();
        return new LeadEntity(
                id,
                lead.tenantId(),
                lead.clientId(),
                lead.status().toString(),
                lead.source(),
                lead.estimatedBudget(),
                lead.notes()
        );
    }

    private static Lead toDomain(LeadEntity entity) {
        return new Lead(
                entity.getId(),
                entity.getTenantId(),
                entity.getClientId(),
                LeadStatus.fromText(entity.getStatus()),
                entity.getSource(),
                entity.getEstimatedBudget(),
                entity.getNotes()
        );
    }
}

