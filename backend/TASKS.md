# Backend Task Overview

This folder contains the Java backend implementation for the Booking + Client CRM.

High-level tasks:

1. Initialize a Spring Boot (or preferred) project using the provided `pom.xml`.
2. Implement entities in `domain/` that correspond to the schema in `docs/SCHEMA.md`.
3. Add repositories and persistence mappings under `infrastructure/persistence/`.
4. Implement application services / use cases under `application/`.
5. Expose REST controllers under `infrastructure/web/` for core flows:
   - Client & Lead management
   - Event / Booking workflow
   - Quote and Invoice lifecycle
6. Wire up security and tenancy resolution to align with the Java Photo Gallery app.
7. Implement scheduled jobs for automation rules and outbound emails.
