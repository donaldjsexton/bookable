package com.example.bookingcrm.infrastructure.web.tenant;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TenantContextFilter extends OncePerRequestFilter {
    public static final String TENANT_HEADER = "X-Tenant-Id";
    private final HandlerExceptionResolver exceptionResolver;

    public TenantContextFilter(HandlerExceptionResolver exceptionResolver) {
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path == null || !path.startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            long tenantId = parseTenantId(request.getHeader(TENANT_HEADER));
            TenantContext.set(tenantId);
            filterChain.doFilter(request, response);
        } catch (RuntimeException ex) {
            exceptionResolver.resolveException(request, response, null, ex);
        } finally {
            TenantContext.clear();
        }
    }

    private static long parseTenantId(String header) {
        if (header == null || header.isBlank()) {
            throw new IllegalArgumentException("Missing required header: " + TENANT_HEADER);
        }
        try {
            long tenantId = Long.parseLong(header.trim());
            if (tenantId <= 0) {
                throw new IllegalArgumentException("Invalid tenantId (must be positive) in header: " + TENANT_HEADER);
            }
            return tenantId;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid tenantId (must be a number) in header: " + TENANT_HEADER);
        }
    }
}
