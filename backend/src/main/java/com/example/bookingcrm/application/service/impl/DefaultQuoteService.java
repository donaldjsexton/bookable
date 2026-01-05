package com.example.bookingcrm.application.service.impl;

import com.example.bookingcrm.application.common.EntityNotFoundException;
import com.example.bookingcrm.application.common.PreconditionFailedException;
import com.example.bookingcrm.application.dto.QuoteDtos.GenerateQuoteCommand;
import com.example.bookingcrm.application.dto.QuoteDtos.LineItem;
import com.example.bookingcrm.application.dto.QuoteDtos.QuoteDto;
import com.example.bookingcrm.application.dto.QuoteDtos.SendQuoteCommand;
import com.example.bookingcrm.application.mapper.QuoteMapper;
import com.example.bookingcrm.application.port.ClientRepository;
import com.example.bookingcrm.application.port.LeadRepository;
import com.example.bookingcrm.application.port.QuoteDelivery;
import com.example.bookingcrm.application.port.QuoteRepository;
import com.example.bookingcrm.application.service.QuoteService;
import com.example.bookingcrm.domain.billing.Quote;
import com.example.bookingcrm.domain.billing.QuoteLineItem;
import com.example.bookingcrm.domain.billing.QuoteStatus;
import com.example.bookingcrm.domain.client.Client;
import com.example.bookingcrm.domain.lead.Lead;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public class DefaultQuoteService implements QuoteService {
    private final QuoteRepository quotes;
    private final LeadRepository leads;
    private final ClientRepository clients;
    private final QuoteDelivery quoteDelivery;

    public DefaultQuoteService(
            QuoteRepository quotes,
            LeadRepository leads,
            ClientRepository clients,
            QuoteDelivery quoteDelivery
    ) {
        this.quotes = quotes;
        this.leads = leads;
        this.clients = clients;
        this.quoteDelivery = quoteDelivery;
    }

    @Override
    @Transactional
    public QuoteDto generate(GenerateQuoteCommand command) {
        Quote created = quotes.save(Quote.newQuote(
                command.tenantId(),
                UUID.randomUUID(),
                command.leadId(),
                command.eventId(),
                command.packageId(),
                QuoteStatus.DRAFT,
                command.validUntil()
        ));

        List<QuoteLineItem> savedItems = quotes.saveLineItems(created.id(), toLineItems(created.id(), command.lineItems()));
        return QuoteMapper.toDto(created, savedItems);
    }

    @Override
    public QuoteDto send(SendQuoteCommand command) {
        Quote quote = quotes.findByTenantIdAndId(command.tenantId(), command.quoteId())
                .orElseThrow(() -> new EntityNotFoundException("Quote not found: tenantId=%s quoteId=%s"
                        .formatted(command.tenantId(), command.quoteId())));

        if (quote.status() != QuoteStatus.DRAFT) {
            throw new PreconditionFailedException("Quote must be DRAFT to send: quoteId=%s status=%s"
                    .formatted(command.quoteId(), quote.status()));
        }

        Long leadId = quote.leadId();
        if (leadId == null) {
            throw new PreconditionFailedException("Quote must be linked to a lead to send: quoteId=%s".formatted(quote.id()));
        }

        Lead lead = leads.findByTenantIdAndId(command.tenantId(), leadId)
                .orElseThrow(() -> new EntityNotFoundException("Lead not found: tenantId=%s leadId=%s"
                        .formatted(command.tenantId(), leadId)));

        Client client = clients.findByTenantIdAndId(command.tenantId(), lead.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found: tenantId=%s clientId=%s"
                        .formatted(command.tenantId(), lead.clientId())));

        List<QuoteLineItem> items = quotes.findLineItemsByQuoteId(quote.id());
        quoteDelivery.sendQuote(command.tenantId(), client, quote, items, command.recipientEmail());

        Quote sent = quotes.save(quote.withStatus(QuoteStatus.SENT));
        return QuoteMapper.toDto(sent, items);
    }

    private static List<QuoteLineItem> toLineItems(long quoteId, List<LineItem> items) {
        if (items == null) {
            return List.of();
        }
        return items.stream()
                .map(item -> QuoteLineItem.newLineItem(
                        quoteId,
                        item.description(),
                        item.quantity(),
                        item.unitPrice(),
                        item.sortOrder()
                ))
                .toList();
    }
}
