package com.example.bookingcrm.infrastructure.web.api;

import com.example.bookingcrm.application.dto.ClientDtos.ClientDto;
import com.example.bookingcrm.application.dto.ClientDtos.CreateClientCommand;
import com.example.bookingcrm.application.dto.ClientDtos.UpdateClientCommand;
import com.example.bookingcrm.application.service.ClientService;
import com.example.bookingcrm.infrastructure.web.tenant.TenantContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService clients;

    public ClientController(ClientService clients) {
        this.clients = clients;
    }

    @PostMapping
    public ClientDto create(@Valid @RequestBody CreateClientRequest request) {
        return clients.create(new CreateClientCommand(
                TenantContext.requireTenantId(),
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phone(),
                request.preferredChannel(),
                request.notes()
        ));
    }

    @PutMapping("/{clientId}")
    public ClientDto update(@PathVariable long clientId, @Valid @RequestBody UpdateClientRequest request) {
        return clients.update(new UpdateClientCommand(
                TenantContext.requireTenantId(),
                clientId,
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phone(),
                request.preferredChannel(),
                request.notes()
        ));
    }

    public record CreateClientRequest(
            @NotBlank String firstName,
            @NotBlank String lastName,
            @Email String email,
            String phone,
            String preferredChannel,
            String notes
    ) {}

    public record UpdateClientRequest(
            @NotBlank String firstName,
            @NotBlank String lastName,
            @Email String email,
            String phone,
            String preferredChannel,
            String notes
    ) {}
}

