package com.example.bookingcrm.infrastructure.web.api;

import com.example.bookingcrm.application.dto.BookingDtos.CreateWorkflowBookingCommand;
import com.example.bookingcrm.application.dto.BookingDtos.WorkflowBookingActionResultDto;
import com.example.bookingcrm.application.service.BookingService;
import com.example.bookingcrm.infrastructure.web.tenant.TenantContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/workflow/actions")
public class WorkflowActionController {
    private final BookingService bookings;

    public WorkflowActionController(BookingService bookings) {
        this.bookings = bookings;
    }

    @PostMapping("/CREATE_BOOKING")
    public WorkflowBookingActionResultDto createBooking(@Valid @RequestBody CreateBookingRequest request) {
        return bookings.createWorkflowBooking(new CreateWorkflowBookingCommand(
                TenantContext.requireTenantId(),
                request.clientName(),
                request.eventType(),
                request.eventDate()
        ));
    }

    public record CreateBookingRequest(
            @NotBlank String clientName,
            @NotBlank String eventType,
            @NotNull LocalDate eventDate
    ) {}
}
