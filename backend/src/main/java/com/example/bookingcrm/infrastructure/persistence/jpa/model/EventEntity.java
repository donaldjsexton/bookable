package com.example.bookingcrm.infrastructure.persistence.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "events")
public class EventEntity extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private long tenantId;

    @Column(name = "public_id", nullable = false)
    private UUID publicId;

    @Column(name = "client_id", nullable = false)
    private long clientId;

    @Column(name = "lead_id")
    private Long leadId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "location_address")
    private String locationAddress;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "gallery_id")
    private Long galleryId;

    @Column(name = "gallery_public_id")
    private UUID galleryPublicId;

    @Column(name = "notes")
    private String notes;

    protected EventEntity() {}

    public EventEntity(
            Long id,
            long tenantId,
            UUID publicId,
            long clientId,
            Long leadId,
            String type,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            String locationName,
            String locationAddress,
            String status,
            Long galleryId,
            UUID galleryPublicId,
            String notes
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.publicId = publicId;
        this.clientId = clientId;
        this.leadId = leadId;
        this.type = type;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.status = status;
        this.galleryId = galleryId;
        this.galleryPublicId = galleryPublicId;
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

    public long getClientId() {
        return clientId;
    }

    public Long getLeadId() {
        return leadId;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public String getStatus() {
        return status;
    }

    public Long getGalleryId() {
        return galleryId;
    }

    public UUID getGalleryPublicId() {
        return galleryPublicId;
    }

    public String getNotes() {
        return notes;
    }
}

