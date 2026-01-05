package com.example.bookingcrm.infrastructure.persistence.adapter;

import com.example.bookingcrm.application.port.PaymentRepository;
import com.example.bookingcrm.domain.billing.Payment;
import com.example.bookingcrm.infrastructure.persistence.jpa.model.PaymentEntity;
import com.example.bookingcrm.infrastructure.persistence.jpa.repo.PaymentJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JpaPaymentRepositoryAdapter implements PaymentRepository {
    private final PaymentJpaRepository payments;

    public JpaPaymentRepositoryAdapter(PaymentJpaRepository payments) {
        this.payments = payments;
    }

    @Override
    public Payment save(Payment payment) {
        PaymentEntity saved = payments.save(toEntity(payment));
        return toDomain(saved);
    }

    private static PaymentEntity toEntity(Payment payment) {
        Long id = payment.id() == 0L ? null : payment.id();
        return new PaymentEntity(
                id,
                payment.invoiceId(),
                payment.amount(),
                payment.method(),
                payment.receivedAt(),
                payment.notes()
        );
    }

    private static Payment toDomain(PaymentEntity entity) {
        return new Payment(
                entity.getId(),
                entity.getInvoiceId(),
                entity.getAmount(),
                entity.getMethod(),
                entity.getReceivedAt(),
                entity.getNotes()
        );
    }
}

