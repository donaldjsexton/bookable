# Web/API Tasks

Tasks:

1. Implement REST controllers for the main resources:
   - `/api/clients`
   - `/api/leads`
   - `/api/events`
   - `/api/quotes`
   - `/api/invoices`
   - `/api/automation-rules`

2. Handle validation and error responses.
3. Resolve the current tenant (e.g., from subdomain or JWT claims) and pass it into
   application services.

Design endpoints so they can later be consumed by:
- An admin dashboard UI.
- A public client portal (view quotes, invoices, and galleries).
