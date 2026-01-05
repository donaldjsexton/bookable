package com.example.bookingcrm.domain.automation;

import java.util.Objects;

public record AutomationRule(
        long id,
        long tenantId,
        String name,
        TriggerType triggerType,
        int offsetDays,
        long templateId,
        boolean isActive
) {
    public AutomationRule {
        if (tenantId <= 0) {
            throw new IllegalArgumentException("tenantId must be positive");
        }
        requireText(name, "name");
        Objects.requireNonNull(triggerType, "triggerType");
        if (templateId <= 0) {
            throw new IllegalArgumentException("templateId must be positive");
        }
    }

    public static AutomationRule newRule(
            long tenantId,
            String name,
            TriggerType triggerType,
            int offsetDays,
            long templateId
    ) {
        return new AutomationRule(0L, tenantId, name, triggerType, offsetDays, templateId, true);
    }

    public AutomationRule activate() {
        if (isActive) {
            return this;
        }
        return new AutomationRule(id, tenantId, name, triggerType, offsetDays, templateId, true);
    }

    public AutomationRule deactivate() {
        if (!isActive) {
            return this;
        }
        return new AutomationRule(id, tenantId, name, triggerType, offsetDays, templateId, false);
    }

    public AutomationRule withSchedule(TriggerType triggerType, int offsetDays) {
        return new AutomationRule(id, tenantId, name, triggerType, offsetDays, templateId, isActive);
    }

    public AutomationRule withTemplate(long templateId) {
        return new AutomationRule(id, tenantId, name, triggerType, offsetDays, templateId, isActive);
    }

    private static void requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
    }
}

