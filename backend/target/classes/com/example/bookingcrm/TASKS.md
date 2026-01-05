# Java Package Tasks

This package contains the Java source code organized into layers:

- `domain` – Entities, value objects, domain services, and domain events.
- `application` – Use cases, orchestrating domain logic and transactions.
- `infrastructure` – Framework adapters: persistence, web controllers, security.

Start by defining entities in `domain/` that match the schema. Then add repositories
and services to support core flows (lead → event → quote → invoice).
