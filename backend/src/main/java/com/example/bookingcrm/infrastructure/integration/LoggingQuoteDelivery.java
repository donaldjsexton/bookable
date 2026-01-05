package com.example.bookingcrm.infrastructure.integration;

import com.example.bookingcrm.application.port.QuoteDelivery;
import com.example.bookingcrm.domain.billing.Quote;
import com.example.bookingcrm.domain.billing.QuoteLineItem;
import com.example.bookingcrm.domain.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LoggingQuoteDelivery implements QuoteDelivery {
    private static final Logger log = LoggerFactory.getLogger(LoggingQuoteDelivery.class);

    @Override
    public void sendQuote(long tenantId, Client client, Quote quote, List<QuoteLineItem> lineItems, String recipientEmail) {
        log.info(
                "QuoteDelivery.sendQuote tenantId={} quoteId={} recipientEmail={} clientId={} items={}",
                tenantId,
                quote.id(),
                recipientEmail,
                client.id(),
                lineItems == null ? 0 : lineItems.size()
        );
    }
}

