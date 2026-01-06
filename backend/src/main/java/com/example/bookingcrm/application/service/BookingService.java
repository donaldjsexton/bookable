package com.example.bookingcrm.application.service;

import com.example.bookingcrm.application.dto.BookingDtos.BookingStateDto;
import com.example.bookingcrm.application.dto.BookingDtos.BookingStateQuery;
import com.example.bookingcrm.application.dto.BookingDtos.BookingWorkflowCandidateDto;
import com.example.bookingcrm.application.dto.BookingDtos.BookingWorkflowCandidatesQuery;
import com.example.bookingcrm.application.dto.BookingDtos.CreateBookingCommand;
import com.example.bookingcrm.application.dto.BookingDtos.CreateWorkflowBookingCommand;
import com.example.bookingcrm.application.dto.BookingDtos.WorkflowBookingActionResultDto;

import java.util.List;

public interface BookingService {
    BookingStateDto getState(BookingStateQuery query);

    BookingWorkflowCandidateDto create(CreateBookingCommand command);

    WorkflowBookingActionResultDto createWorkflowBooking(CreateWorkflowBookingCommand command);

    List<BookingWorkflowCandidateDto> listWorkflowCandidates(BookingWorkflowCandidatesQuery query);
}
