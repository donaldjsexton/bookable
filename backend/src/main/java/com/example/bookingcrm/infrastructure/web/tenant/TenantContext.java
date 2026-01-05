package com.example.bookingcrm.infrastructure.web.tenant;

public final class TenantContext {
    private static final ThreadLocal<Long> CURRENT_TENANT_ID = new ThreadLocal<>();

    private TenantContext() {}

    public static void set(long tenantId) {
        CURRENT_TENANT_ID.set(tenantId);
    }

    public static Long get() {
        return CURRENT_TENANT_ID.get();
    }

    public static long requireTenantId() {
        Long tenantId = CURRENT_TENANT_ID.get();
        if (tenantId == null) {
            throw new IllegalStateException("tenantId not resolved for request");
        }
        return tenantId;
    }

    public static void clear() {
        CURRENT_TENANT_ID.remove();
    }
}

