package com.example.bookingcrm.application.service;

import com.example.bookingcrm.application.dto.InvoiceDtos.CreateInvoiceCommand;
import com.example.bookingcrm.application.dto.InvoiceDtos.InvoiceDto;
import com.example.bookingcrm.application.dto.InvoiceDtos.PaymentDto;
import com.example.bookingcrm.application.dto.InvoiceDtos.RegisterPaymentCommand;

public interface InvoiceService {
    InvoiceDto create(CreateInvoiceCommand command);

    PaymentDto registerPayment(RegisterPaymentCommand command);
}

