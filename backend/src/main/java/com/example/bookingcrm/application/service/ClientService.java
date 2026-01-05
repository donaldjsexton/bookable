package com.example.bookingcrm.application.service;

import com.example.bookingcrm.application.dto.ClientDtos.ClientDto;
import com.example.bookingcrm.application.dto.ClientDtos.CreateClientCommand;
import com.example.bookingcrm.application.dto.ClientDtos.UpdateClientCommand;

public interface ClientService {
    ClientDto create(CreateClientCommand command);

    ClientDto update(UpdateClientCommand command);
}

