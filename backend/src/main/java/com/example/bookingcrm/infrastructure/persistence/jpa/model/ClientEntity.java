package com.example.bookingcrm.infrastructure.persistence.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "clients")
public class ClientEntity extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private long tenantId;

    @Column(name = "public_id", nullable = false)
    private UUID publicId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "preferred_channel")
    private String preferredChannel;

    @Column(name = "notes")
    private String notes;

    protected ClientEntity() {}

    public ClientEntity(
            Long id,
            long tenantId,
            UUID publicId,
            String firstName,
            String lastName,
            String email,
            String phone,
            String preferredChannel,
            String notes
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.publicId = publicId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.preferredChannel = preferredChannel;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public long getTenantId() {
        return tenantId;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPreferredChannel() {
        return preferredChannel;
    }

    public String getNotes() {
        return notes;
    }
}

