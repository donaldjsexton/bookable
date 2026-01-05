package com.example.bookingcrm.application.service;

import com.example.bookingcrm.application.dto.EventDtos.EventDto;
import com.example.bookingcrm.application.dto.LeadDtos.ConvertLeadToEventCommand;
import com.example.bookingcrm.application.dto.LeadDtos.CreateLeadCommand;
import com.example.bookingcrm.application.dto.LeadDtos.LeadDto;
import com.example.bookingcrm.application.dto.LeadDtos.UpdateLeadCommand;

public interface LeadService {
    LeadDto create(CreateLeadCommand command);

    LeadDto update(UpdateLeadCommand command);

    EventDto convertToEvent(ConvertLeadToEventCommand command);
}

