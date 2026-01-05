package com.example.bookingcrm.application.port;

import com.example.bookingcrm.domain.billing.Quote;
import com.example.bookingcrm.domain.billing.QuoteLineItem;

import java.util.List;
import java.util.Optional;

public interface QuoteRepository {
    Quote save(Quote quote);

    Optional<Quote> findByTenantIdAndId(long tenantId, long quoteId);

    List<QuoteLineItem> saveLineItems(long quoteId, List<QuoteLineItem> lineItems);

    List<QuoteLineItem> findLineItemsByQuoteId(long quoteId);
}
