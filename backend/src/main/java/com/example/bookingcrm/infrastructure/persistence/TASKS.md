# Persistence Tasks

Tasks:

1. Map domain entities to JPA entities (or other persistence models).
2. Create Spring Data repositories or equivalent interfaces.
3. Implement tenant-aware queries (always filter by `tenantId`).
4. Add migration scripts (Flyway) to keep the database in sync with the schema.

See `src/main/resources/db/migration/V1__initial_schema.sql` for the starting point.
