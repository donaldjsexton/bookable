# Domain Layer Tasks

The domain layer should be as framework-free as possible.

Tasks:

1. Define entity classes for:
   - Client
   - ClientTag / ClientTagAssignment
   - Lead
   - Event
   - Package
   - Quote / QuoteLineItem
   - Invoice
   - Payment
   - AutomationRule
   - EmailTemplate
   - ActivityLog

2. Add enums and value objects as needed:
   - LeadStatus
   - EventStatus
   - QuoteStatus
   - InvoiceStatus
   - TriggerType (for automation)
   - Money / EmailAddress (optional)

3. Capture core invariants and business rules inside constructors and methods
   (e.g., updating invoice status when payments are applied).
