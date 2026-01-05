package com.example.bookingcrm.application.mapper;

import com.example.bookingcrm.application.dto.QuoteDtos.QuoteDto;
import com.example.bookingcrm.application.dto.QuoteDtos.QuoteLineItemDto;
import com.example.bookingcrm.domain.billing.Quote;
import com.example.bookingcrm.domain.billing.QuoteLineItem;

import java.util.List;

public final class QuoteMapper {
    private QuoteMapper() {}

    public static QuoteDto toDto(Quote quote, List<QuoteLineItem> lineItems) {
        return new QuoteDto(
                quote.id(),
                quote.tenantId(),
                quote.publicId(),
                quote.leadId(),
                quote.eventId(),
                quote.packageId(),
                quote.status().name(),
                quote.validUntil(),
                lineItems.stream().map(QuoteMapper::toDto).toList()
        );
    }

    public static QuoteLineItemDto toDto(QuoteLineItem item) {
        return new QuoteLineItemDto(
                item.id(),
                item.description(),
                item.quantity(),
                item.unitPrice(),
                item.sortOrder()
        );
    }
}
