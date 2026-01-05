package com.example.bookingcrm.application.service.impl;

import com.example.bookingcrm.application.common.EntityNotFoundException;
import com.example.bookingcrm.application.dto.EventDtos.EventDto;
import com.example.bookingcrm.application.dto.LeadDtos.ConvertLeadToEventCommand;
import com.example.bookingcrm.application.dto.LeadDtos.CreateLeadCommand;
import com.example.bookingcrm.application.dto.LeadDtos.LeadDto;
import com.example.bookingcrm.application.dto.LeadDtos.UpdateLeadCommand;
import com.example.bookingcrm.application.mapper.EventMapper;
import com.example.bookingcrm.application.mapper.LeadMapper;
import com.example.bookingcrm.application.port.EventRepository;
import com.example.bookingcrm.application.port.LeadRepository;
import com.example.bookingcrm.application.service.LeadService;
import com.example.bookingcrm.domain.event.Event;
import com.example.bookingcrm.domain.event.EventType;
import com.example.bookingcrm.domain.lead.Lead;
import com.example.bookingcrm.domain.lead.LeadStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class DefaultLeadService implements LeadService {
    private final LeadRepository leads;
    private final EventRepository events;

    public DefaultLeadService(LeadRepository leads, EventRepository events) {
        this.leads = leads;
        this.events = events;
    }

    @Override
    public LeadDto create(CreateLeadCommand command) {
        Lead created = leads.save(Lead.newLead(
                command.tenantId(),
                command.clientId(),
                LeadStatus.fromText(command.status()),
                command.source(),
                command.estimatedBudget(),
                command.notes()
        ));
        return LeadMapper.toDto(created);
    }

    @Override
    public LeadDto update(UpdateLeadCommand command) {
        Lead existing = leads.findByTenantIdAndId(command.tenantId(), command.leadId())
                .orElseThrow(() -> new EntityNotFoundException("Lead not found: tenantId=%s leadId=%s"
                        .formatted(command.tenantId(), command.leadId())));

        Lead updated = leads.save(existing.withDetails(
                LeadStatus.fromText(command.status()),
                command.source(),
                command.estimatedBudget(),
                command.notes()
        ));
        return LeadMapper.toDto(updated);
    }

    @Override
    @Transactional
    public EventDto convertToEvent(ConvertLeadToEventCommand command) {
        Lead lead = leads.findByTenantIdAndId(command.tenantId(), command.leadId())
                .orElseThrow(() -> new EntityNotFoundException("Lead not found: tenantId=%s leadId=%s"
                        .formatted(command.tenantId(), command.leadId())));

        Lead converted = leads.save(lead.markBooked());
        EventType eventType = EventType.fromText(command.eventType());
        Event createdEvent = events.save(Event.newFromLead(
                converted.tenantId(),
                UUID.randomUUID(),
                converted.clientId(),
                converted.id(),
                eventType,
                command.eventDate()
        ));
        return EventMapper.toDto(createdEvent);
    }
}
