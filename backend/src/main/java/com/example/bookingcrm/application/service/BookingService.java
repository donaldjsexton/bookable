package com.example.bookingcrm.application.service;

import com.example.bookingcrm.application.dto.BookingDtos.BookingStateDto;
import com.example.bookingcrm.application.dto.BookingDtos.BookingStateQuery;
import com.example.bookingcrm.application.dto.BookingDtos.BookingWorkflowCandidateDto;
import com.example.bookingcrm.application.dto.BookingDtos.BookingWorkflowCandidatesQuery;

import java.util.List;

public interface BookingService {
    BookingStateDto getState(BookingStateQuery query);

    List<BookingWorkflowCandidateDto> listWorkflowCandidates(BookingWorkflowCandidatesQuery query);
}
