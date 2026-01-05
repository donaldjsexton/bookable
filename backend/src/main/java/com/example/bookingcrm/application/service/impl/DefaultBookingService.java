package com.example.bookingcrm.application.service.impl;

import com.example.bookingcrm.application.common.EntityNotFoundException;
import com.example.bookingcrm.application.dto.BookingDtos.BookingStateDto;
import com.example.bookingcrm.application.dto.BookingDtos.BookingStateQuery;
import com.example.bookingcrm.application.dto.BookingDtos.BookingWorkflowCandidateDto;
import com.example.bookingcrm.application.dto.BookingDtos.BookingWorkflowCandidatesQuery;
import com.example.bookingcrm.application.port.BookingRepository;
import com.example.bookingcrm.application.port.ClientRepository;
import com.example.bookingcrm.application.port.EventRepository;
import com.example.bookingcrm.application.port.InvoiceRepository;
import com.example.bookingcrm.application.port.QuoteRepository;
import com.example.bookingcrm.application.service.BookingService;
import com.example.bookingcrm.domain.billing.Invoice;
import com.example.bookingcrm.domain.billing.InvoiceStatus;
import com.example.bookingcrm.domain.billing.Quote;
import com.example.bookingcrm.domain.billing.QuoteStatus;
import com.example.bookingcrm.domain.booking.Booking;
import com.example.bookingcrm.domain.booking.BookingContext;
import com.example.bookingcrm.domain.booking.BookingStateMachine;
import com.example.bookingcrm.domain.client.Client;
import com.example.bookingcrm.domain.event.Event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class DefaultBookingService implements BookingService {
    private static final DateTimeFormatter WORKFLOW_LABEL_DATE =
            DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
    private final BookingRepository bookings;
    private final ClientRepository clients;
    private final EventRepository events;
    private final QuoteRepository quotes;
    private final InvoiceRepository invoices;
    private final BookingStateMachine stateMachine = new BookingStateMachine();

    public DefaultBookingService(
            BookingRepository bookings,
            ClientRepository clients,
            EventRepository events,
            QuoteRepository quotes,
            InvoiceRepository invoices
    ) {
        this.bookings = bookings;
        this.clients = clients;
        this.events = events;
        this.quotes = quotes;
        this.invoices = invoices;
    }

    @Override
    public BookingStateDto getState(BookingStateQuery query) {
        Booking booking = bookings.findByTenantIdAndId(query.tenantId(), query.bookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        Client client = clients.findByTenantIdAndId(query.tenantId(), booking.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        Event event = loadEvent(query.tenantId(), booking);
        Quote quote = loadQuote(query.tenantId(), booking);
        Invoice invoice = loadInvoice(query.tenantId(), booking);

        BookingContext context = new BookingContext(
                hasClientEmail(client),
                booking.consultDate() != null,
                quote != null,
                quote != null && quote.status() == QuoteStatus.ACCEPTED,
                invoice != null && isDepositRecorded(invoice),
                event != null && hasEventPassed(event),
                event != null && hasImagesUploaded(event),
                invoice != null && invoice.status() == InvoiceStatus.PAID
        );

        List<String> allowedActions = stateMachine.allowedActions(booking.state(), context).stream()
                .map(Enum::name)
                .toList();

        return new BookingStateDto(booking.state().name(), allowedActions);
    }

    @Override
    public List<BookingWorkflowCandidateDto> listWorkflowCandidates(BookingWorkflowCandidatesQuery query) {
        return bookings.findByTenantId(query.tenantId()).stream()
                .map(booking -> toWorkflowCandidate(query.tenantId(), booking))
                .toList();
    }

    private BookingWorkflowCandidateDto toWorkflowCandidate(long tenantId, Booking booking) {
        String clientLabel = clients.findByTenantIdAndId(tenantId, booking.clientId())
                .map(client -> client.firstName() + " " + client.lastName())
                .orElse("Client " + booking.clientId());

        Event event = null;
        if (booking.eventId() != null) {
            event = events.findByTenantIdAndId(tenantId, booking.eventId()).orElse(null);
        }

        String label = buildWorkflowCandidateLabel(clientLabel, booking, event);
        return new BookingWorkflowCandidateDto(booking.id(), label, booking.state().name());
    }

    private static String buildWorkflowCandidateLabel(String clientLabel, Booking booking, Event event) {
        String eventType = event != null ? humanizeEnum(event.type()) : "Unknown Event";
        String eventDate = formatEventDate(event, booking);
        return "%s — %s — %s".formatted(clientLabel, eventType, eventDate);
    }

    private static String formatEventDate(Event event, Booking booking) {
        if (event != null) {
            return event.date().format(WORKFLOW_LABEL_DATE);
        }
        if (booking.consultDate() != null) {
            return booking.consultDate().format(WORKFLOW_LABEL_DATE);
        }
        return "TBD";
    }

    private static String humanizeEnum(Enum<?> value) {
        String raw = value.name().toLowerCase(Locale.ROOT);
        String[] parts = raw.split("_");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return builder.length() > 0 ? builder.toString() : value.name();
    }

    private Event loadEvent(long tenantId, Booking booking) {
        if (booking.eventId() == null) {
            return null;
        }
        return events.findByTenantIdAndId(tenantId, booking.eventId())
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
    }

    private Quote loadQuote(long tenantId, Booking booking) {
        if (booking.quoteId() == null) {
            return null;
        }
        return quotes.findByTenantIdAndId(tenantId, booking.quoteId())
                .orElseThrow(() -> new EntityNotFoundException("Quote not found"));
    }

    private Invoice loadInvoice(long tenantId, Booking booking) {
        if (booking.invoiceId() == null) {
            return null;
        }
        return invoices.findByTenantIdAndId(tenantId, booking.invoiceId())
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));
    }

    private static boolean hasClientEmail(Client client) {
        return client.email() != null && !client.email().isBlank();
    }

    private static boolean isDepositRecorded(Invoice invoice) {
        return invoice.status() == InvoiceStatus.PARTIALLY_PAID || invoice.status() == InvoiceStatus.PAID;
    }

    private static boolean hasEventPassed(Event event) {
        return event.date().isBefore(LocalDate.now());
    }

    private static boolean hasImagesUploaded(Event event) {
        return event.galleryId() != null || event.galleryPublicId() != null;
    }
}
