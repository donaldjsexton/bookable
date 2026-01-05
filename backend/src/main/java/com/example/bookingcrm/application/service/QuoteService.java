package com.example.bookingcrm.application.service;

import com.example.bookingcrm.application.dto.QuoteDtos.GenerateQuoteCommand;
import com.example.bookingcrm.application.dto.QuoteDtos.QuoteDto;
import com.example.bookingcrm.application.dto.QuoteDtos.SendQuoteCommand;

public interface QuoteService {
    QuoteDto generate(GenerateQuoteCommand command);

    QuoteDto send(SendQuoteCommand command);
}

