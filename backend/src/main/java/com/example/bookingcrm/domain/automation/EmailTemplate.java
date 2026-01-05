package com.example.bookingcrm.domain.automation;

import java.util.Objects;

public record EmailTemplate(
        long id,
        long tenantId,
        String name,
        String subject,
        String body,
        String description
) {
    public EmailTemplate {
        if (tenantId <= 0) {
            throw new IllegalArgumentException("tenantId must be positive");
        }
        requireText(name, "name");
        requireText(subject, "subject");
        requireText(body, "body");
    }

    public static EmailTemplate newTemplate(
            long tenantId,
            String name,
            String subject,
            String body,
            String description
    ) {
        return new EmailTemplate(0L, tenantId, name, subject, body, description);
    }

    public EmailTemplate withContent(String subject, String body) {
        return new EmailTemplate(id, tenantId, name, subject, body, description);
    }

    public EmailTemplate withName(String name) {
        return new EmailTemplate(id, tenantId, name, subject, body, description);
    }

    public EmailTemplate withDescription(String description) {
        return new EmailTemplate(id, tenantId, name, subject, body, description);
    }

    private static void requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
    }
}

