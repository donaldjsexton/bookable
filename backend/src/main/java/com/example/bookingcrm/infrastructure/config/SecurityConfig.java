package com.example.bookingcrm.infrastructure.config;

import com.example.bookingcrm.infrastructure.web.tenant.TenantContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class SecurityConfig {
    @Bean
    public TenantContextFilter tenantContextFilter(HandlerExceptionResolver handlerExceptionResolver) {
        return new TenantContextFilter(handlerExceptionResolver);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, TenantContextFilter tenantContextFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(tenantContextFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
