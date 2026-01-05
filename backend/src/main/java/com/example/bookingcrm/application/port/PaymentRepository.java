package com.example.bookingcrm.application.port;

import com.example.bookingcrm.domain.billing.Payment;

public interface PaymentRepository {
    Payment save(Payment payment);
}
