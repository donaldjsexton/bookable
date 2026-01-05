# Domain Model Tasks

This folder contains **framework-free** building blocks shared across the domain.

- Add reusable domain helpers (preconditions, shared value objects, small primitives).
- Keep types immutable and side-effect free.
- Prefer small records/value objects over inheritance-heavy hierarchies.

## Suggested building blocks

- `DomainPreconditions` – shared `require*` helpers (`requireText`, `requirePositive`, etc.).
- `PublicId` – UUID wrapper for externally visible identifiers.
- `AuditFields` – created/updated timestamps with invariant checks.

