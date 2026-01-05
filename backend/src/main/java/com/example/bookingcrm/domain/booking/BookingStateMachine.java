package com.example.bookingcrm.domain.booking;

import com.example.bookingcrm.domain.model.DomainPreconditions;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public final class BookingStateMachine {
    private static final Map<BookingState, EnumMap<BookingAction, BookingState>> TRANSITIONS = buildTransitions();
    private static final Map<BookingAction, Predicate<BookingContext>> PRECONDITIONS = buildPreconditions();

    public boolean canTransition(BookingState currentState, BookingAction action, BookingContext context) {
        DomainPreconditions.requireNonNull(currentState, "currentState");
        DomainPreconditions.requireNonNull(action, "action");
        DomainPreconditions.requireNonNull(context, "context");
        return nextState(currentState, action)
                .filter(state -> meetsPrecondition(action, context))
                .isPresent();
    }

    public BookingState transition(BookingState currentState, BookingAction action, BookingContext context) {
        DomainPreconditions.requireNonNull(currentState, "currentState");
        DomainPreconditions.requireNonNull(action, "action");
        DomainPreconditions.requireNonNull(context, "context");
        BookingState nextState = nextState(currentState, action)
                .orElseThrow(() -> new IllegalArgumentException(
                        "invalid transition: %s -> %s".formatted(currentState, action)
                ));
        if (!meetsPrecondition(action, context)) {
            throw new IllegalStateException("precondition failed for action: " + action);
        }
        return nextState;
    }

    public List<BookingAction> allowedActions(BookingState currentState, BookingContext context) {
        DomainPreconditions.requireNonNull(currentState, "currentState");
        DomainPreconditions.requireNonNull(context, "context");
        return Arrays.stream(BookingAction.values())
                .filter(action -> canTransition(currentState, action, context))
                .toList();
    }

    private static boolean meetsPrecondition(BookingAction action, BookingContext context) {
        Predicate<BookingContext> guard = PRECONDITIONS.get(action);
        return guard == null || guard.test(context);
    }

    private static java.util.Optional<BookingState> nextState(BookingState currentState, BookingAction action) {
        EnumMap<BookingAction, BookingState> actions = TRANSITIONS.get(currentState);
        if (actions == null) {
            return java.util.Optional.empty();
        }
        return java.util.Optional.ofNullable(actions.get(action));
    }

    private static Map<BookingState, EnumMap<BookingAction, BookingState>> buildTransitions() {
        EnumMap<BookingState, EnumMap<BookingAction, BookingState>> transitions = new EnumMap<>(BookingState.class);
        addTransition(transitions, BookingState.INQUIRY, BookingAction.CONTACT_LEAD, BookingState.CONTACTED);
        addTransition(transitions, BookingState.CONTACTED, BookingAction.SCHEDULE_CONSULT, BookingState.CONSULT_SCHEDULED);
        addTransition(transitions, BookingState.CONSULT_SCHEDULED, BookingAction.SEND_PROPOSAL, BookingState.PROPOSAL_SENT);
        addTransition(transitions, BookingState.PROPOSAL_SENT, BookingAction.ACCEPT_PROPOSAL, BookingState.BOOKED);
        addTransition(transitions, BookingState.BOOKED, BookingAction.RECORD_DEPOSIT, BookingState.DEPOSIT_PAID);
        addTransition(transitions, BookingState.DEPOSIT_PAID, BookingAction.COMPLETE_EVENT, BookingState.EVENT_COMPLETED);
        addTransition(transitions, BookingState.EVENT_COMPLETED, BookingAction.START_DELIVERY, BookingState.DELIVERY_IN_PROGRESS);
        addTransition(transitions, BookingState.DELIVERY_IN_PROGRESS, BookingAction.SEND_GALLERY, BookingState.DELIVERED);
        addTransition(transitions, BookingState.DELIVERED, BookingAction.CLOSE_BOOKING, BookingState.CLOSED);

        addTransition(transitions, BookingState.INQUIRY, BookingAction.MARK_LOST, BookingState.LOST);
        addTransition(transitions, BookingState.PROPOSAL_SENT, BookingAction.MARK_LOST, BookingState.LOST);
        addTransition(transitions, BookingState.CLOSED, BookingAction.ARCHIVE, BookingState.ARCHIVED);

        for (BookingState state : BookingState.values()) {
            addTransition(transitions, state, BookingAction.CANCEL, BookingState.CANCELLED);
        }

        return transitions;
    }

    private static Map<BookingAction, Predicate<BookingContext>> buildPreconditions() {
        EnumMap<BookingAction, Predicate<BookingContext>> preconditions = new EnumMap<>(BookingAction.class);
        preconditions.put(BookingAction.CONTACT_LEAD, BookingContext::hasClientEmail);
        preconditions.put(BookingAction.SCHEDULE_CONSULT, BookingContext::hasConsultDate);
        preconditions.put(BookingAction.SEND_PROPOSAL, BookingContext::hasQuote);
        preconditions.put(BookingAction.ACCEPT_PROPOSAL, BookingContext::proposalSigned);
        preconditions.put(BookingAction.RECORD_DEPOSIT, BookingContext::depositRecorded);
        preconditions.put(BookingAction.COMPLETE_EVENT, BookingContext::eventDatePassed);
        preconditions.put(BookingAction.START_DELIVERY, BookingContext::imagesUploaded);
        preconditions.put(BookingAction.SEND_GALLERY, BookingContext::imagesUploaded);
        preconditions.put(BookingAction.CLOSE_BOOKING, BookingContext::finalPaymentRecorded);
        return preconditions;
    }

    private static void addTransition(
            Map<BookingState, EnumMap<BookingAction, BookingState>> transitions,
            BookingState from,
            BookingAction action,
            BookingState to
    ) {
        transitions.computeIfAbsent(from, key -> new EnumMap<>(BookingAction.class)).put(action, to);
    }
}
