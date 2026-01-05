package com.example.bookingcrm.infrastructure.web.api;

import com.example.bookingcrm.application.dto.EventDtos.EventDto;
import com.example.bookingcrm.application.dto.LeadDtos.ConvertLeadToEventCommand;
import com.example.bookingcrm.application.dto.LeadDtos.CreateLeadCommand;
import com.example.bookingcrm.application.dto.LeadDtos.LeadDto;
import com.example.bookingcrm.application.dto.LeadDtos.UpdateLeadCommand;
import com.example.bookingcrm.application.service.LeadService;
import com.example.bookingcrm.infrastructure.web.tenant.TenantContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/leads")
public class LeadController {
    private final LeadService leads;

    public LeadController(LeadService leads) {
        this.leads = leads;
    }

    @PostMapping
    public LeadDto create(@Valid @RequestBody CreateLeadRequest request) {
        return leads.create(new CreateLeadCommand(
                TenantContext.requireTenantId(),
                request.clientId(),
                request.status(),
                request.source(),
                request.estimatedBudget(),
                request.notes()
        ));
    }

    @PutMapping("/{leadId}")
    public LeadDto update(@PathVariable long leadId, @Valid @RequestBody UpdateLeadRequest request) {
        return leads.update(new UpdateLeadCommand(
                TenantContext.requireTenantId(),
                leadId,
                request.status(),
                request.source(),
                request.estimatedBudget(),
                request.notes()
        ));
    }

    @PostMapping("/{leadId}/convert-to-event")
    public EventDto convertToEvent(@PathVariable long leadId, @Valid @RequestBody ConvertToEventRequest request) {
        return leads.convertToEvent(new ConvertLeadToEventCommand(
                TenantContext.requireTenantId(),
                leadId,
                request.eventType(),
                request.eventDate()
        ));
    }

    public record CreateLeadRequest(
            @Positive long clientId,
            @NotBlank String status,
            String source,
            BigDecimal estimatedBudget,
            String notes
    ) {}

    public record UpdateLeadRequest(
            @NotBlank String status,
            String source,
            BigDecimal estimatedBudget,
            String notes
    ) {}

    public record ConvertToEventRequest(
            @NotBlank String eventType,
            @NotNull LocalDate eventDate
    ) {}
}

