# Common Domain Tasks

This folder contains domain types for the **common** context.

- Add entity classes, enums, and value objects related to `common` (shared across contexts).
- Keep these classes free of Spring annotations.
- Implement core business rules (methods that enforce invariants).

## Suggested building blocks

- Value objects:
  - `Money` (amount normalization/scale; arithmetic helpers)
  - `EmailAddress` (normalization + basic validation)
  - `PhoneNumber` (basic normalization/validation)
  - `PublicId` (UUID wrapper) (optional)
  - `AuditFields` (createdAt/updatedAt) (optional)
- Simple shared validation helpers (e.g., `requireText`, `requirePositive`) (optional)

Refer to `docs/SCHEMA.md` for initial fields and relationships.
