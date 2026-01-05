# Booking + Client CRM (Java) – Skeleton

This is a skeleton project for a Booking + Client CRM service designed to:

- Run as a standalone Java backend.
- Later integrate cleanly with the existing Java Photo Gallery application via shared tenants and gallery linkage.
- Act as a revenue and retention engine (leads → bookings → invoices → automation).

The structure follows a layered / hexagonal style:

- `domain/` – pure domain objects and business logic.
- `application/` – use cases / services orchestrating domain logic.
- `infrastructure/` – persistence, web, configuration, and frameworks (e.g., Spring Boot).
- `docs/` – project documentation, including schema and design notes.

You can import this as a Maven project (Spring Boot 3.x) and incrementally fill in the code.
