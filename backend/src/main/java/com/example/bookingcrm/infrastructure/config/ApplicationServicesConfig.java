package com.example.bookingcrm.infrastructure.config;

import com.example.bookingcrm.application.port.BookingRepository;
import com.example.bookingcrm.application.port.ClientRepository;
import com.example.bookingcrm.application.port.EventRepository;
import com.example.bookingcrm.application.port.InvoiceRepository;
import com.example.bookingcrm.application.port.LeadRepository;
import com.example.bookingcrm.application.port.PaymentRepository;
import com.example.bookingcrm.application.port.QuoteDelivery;
import com.example.bookingcrm.application.port.QuoteRepository;
import com.example.bookingcrm.application.service.BookingService;
import com.example.bookingcrm.application.service.ClientService;
import com.example.bookingcrm.application.service.InvoiceService;
import com.example.bookingcrm.application.service.LeadService;
import com.example.bookingcrm.application.service.QuoteService;
import com.example.bookingcrm.application.service.impl.DefaultBookingService;
import com.example.bookingcrm.application.service.impl.DefaultClientService;
import com.example.bookingcrm.application.service.impl.DefaultInvoiceService;
import com.example.bookingcrm.application.service.impl.DefaultLeadService;
import com.example.bookingcrm.application.service.impl.DefaultQuoteService;
import com.example.bookingcrm.infrastructure.integration.LoggingQuoteDelivery;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationServicesConfig {
    @Bean
    public ClientService clientService(ClientRepository clients) {
        return new DefaultClientService(clients);
    }

    @Bean
    public LeadService leadService(LeadRepository leads, EventRepository events) {
        return new DefaultLeadService(leads, events);
    }

    @Bean
    public BookingService bookingService(
            BookingRepository bookings,
            ClientRepository clients,
            EventRepository events,
            QuoteRepository quotes,
            InvoiceRepository invoices
    ) {
        return new DefaultBookingService(bookings, clients, events, quotes, invoices);
    }

    @Bean
    public QuoteService quoteService(
            QuoteRepository quotes,
            LeadRepository leads,
            ClientRepository clients,
            QuoteDelivery quoteDelivery
    ) {
        return new DefaultQuoteService(quotes, leads, clients, quoteDelivery);
    }

    @Bean
    public InvoiceService invoiceService(InvoiceRepository invoices, PaymentRepository payments) {
        return new DefaultInvoiceService(invoices, payments);
    }

    @Bean
    public QuoteDelivery quoteDelivery() {
        return new LoggingQuoteDelivery();
    }
}
