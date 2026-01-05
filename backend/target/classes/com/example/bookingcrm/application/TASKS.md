# Application Layer Tasks

The application layer coordinates domain objects to perform use cases.

Tasks:

1. Define service interfaces and implementations for core flows:
   - Create / update Client and Lead.
   - Convert Lead to Event (booking).
   - Generate Quote and send it.
   - Create Invoice and register Payments.

2. Keep business logic in the domain layer; keep this layer thin where possible,
   focusing on orchestration and transactions.

3. Add DTOs and mappers as needed for communication with web/API layer.
