package com.example.bookingcrm.application.port;

import com.example.bookingcrm.domain.client.Client;
import com.example.bookingcrm.domain.billing.Quote;
import com.example.bookingcrm.domain.billing.QuoteLineItem;

import java.util.List;

public interface QuoteDelivery {
    void sendQuote(long tenantId, Client client, Quote quote, List<QuoteLineItem> lineItems, String recipientEmail);
}
