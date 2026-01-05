# Infrastructure Layer Tasks

The infrastructure layer connects the domain/application code to the outside world.

It should contain:

- Persistence implementations (JPA entities, repositories, and mappers).
- Web/API controllers and request/response models.
- Security configuration and tenant resolution.
- Integration clients (e.g., outbound calls to the Photo Gallery service).

Keep dependencies on frameworks here, not in the domain layer.
