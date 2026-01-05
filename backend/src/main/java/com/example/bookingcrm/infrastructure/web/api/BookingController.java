package com.example.bookingcrm.infrastructure.web.api;

import com.example.bookingcrm.application.dto.BookingDtos.BookingStateDto;
import com.example.bookingcrm.application.dto.BookingDtos.BookingStateQuery;
import com.example.bookingcrm.application.dto.BookingDtos.BookingWorkflowCandidateDto;
import com.example.bookingcrm.application.dto.BookingDtos.BookingWorkflowCandidatesQuery;
import com.example.bookingcrm.application.service.BookingService;
import com.example.bookingcrm.infrastructure.web.tenant.TenantContext;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookings;

    public BookingController(BookingService bookings) {
        this.bookings = bookings;
    }

    @GetMapping("/{bookingId}/state")
    public BookingStateDto state(@PathVariable @Positive long bookingId) {
        return bookings.getState(new BookingStateQuery(TenantContext.requireTenantId(), bookingId));
    }

    @GetMapping("/workflow-candidates")
    public List<BookingWorkflowCandidateDto> workflowCandidates() {
        return bookings.listWorkflowCandidates(
                new BookingWorkflowCandidatesQuery(TenantContext.requireTenantId())
        );
    }
}
