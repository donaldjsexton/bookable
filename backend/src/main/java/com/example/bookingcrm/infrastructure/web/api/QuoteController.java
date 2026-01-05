package com.example.bookingcrm.infrastructure.web.api;

import com.example.bookingcrm.application.dto.QuoteDtos.GenerateQuoteCommand;
import com.example.bookingcrm.application.dto.QuoteDtos.LineItem;
import com.example.bookingcrm.application.dto.QuoteDtos.QuoteDto;
import com.example.bookingcrm.application.dto.QuoteDtos.SendQuoteCommand;
import com.example.bookingcrm.application.service.QuoteService;
import com.example.bookingcrm.infrastructure.web.tenant.TenantContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {
    private final QuoteService quotes;

    public QuoteController(QuoteService quotes) {
        this.quotes = quotes;
    }

    @PostMapping
    public QuoteDto generate(@Valid @RequestBody GenerateQuoteRequest request) {
        List<LineItem> lineItems = request.lineItems() == null
                ? List.of()
                : request.lineItems().stream().map(LineItemRequest::toLineItem).toList();

        return quotes.generate(new GenerateQuoteCommand(
                TenantContext.requireTenantId(),
                request.leadId(),
                request.eventId(),
                request.packageId(),
                request.validUntil(),
                lineItems
        ));
    }

    @PostMapping("/{quoteId}/send")
    public QuoteDto send(@PathVariable long quoteId, @Valid @RequestBody SendQuoteRequest request) {
        return quotes.send(new SendQuoteCommand(
                TenantContext.requireTenantId(),
                quoteId,
                request.recipientEmail()
        ));
    }

    public record GenerateQuoteRequest(
            Long leadId,
            Long eventId,
            Long packageId,
            LocalDate validUntil,
            @Valid List<LineItemRequest> lineItems
    ) {}

    public record LineItemRequest(
            @NotBlank String description,
            @Positive int quantity,
            @NotNull BigDecimal unitPrice,
            int sortOrder
    ) {
        LineItem toLineItem() {
            return new LineItem(description, quantity, unitPrice, sortOrder);
        }
    }

    public record SendQuoteRequest(@NotBlank @Email String recipientEmail) {}
}
