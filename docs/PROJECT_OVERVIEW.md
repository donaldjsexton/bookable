# Project Overview

This service manages:

- **Clients & Leads** – contact information, lead sources, qualification status.
- **Events / Bookings** – weddings, sessions, or other jobs with date, time, and location.
- **Packages & Quotes** – standardized offerings and customizable proposals.
- **Invoices & Payments** – basic billing, payment tracking, and revenue reports.
- **Automation Rules** – time-based follow-ups for retention and reviews.
- **Activity Log** – audit trail of key actions (lead created, quote sent, invoice paid, etc.).

## Integration Goals

- Use `tenant_id` on all entities to align with the existing multi-tenant Java Photo Gallery.
- Allow linking an `Event` to a `Gallery` using `gallery_id` and/or `gallery_public_id`.
- Support future cross-links between systems (e.g., "View Gallery" from CRM and "View Booking" from Gallery).
