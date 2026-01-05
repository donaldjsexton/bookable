# Database Schema (Initial Draft)

All tables are assumed to be **tenant-scoped** using `tenant_id`. The `tenant_id` is expected
to reference an existing `tenants` table (from the photo gallery application) or an equivalent
multi-tenant structure. Foreign keys to `tenants` can be added once this service shares the
same database.

## Tables

### clients

- `id` BIGSERIAL PK
- `tenant_id` BIGINT NOT NULL
- `public_id` UUID NOT NULL
- `first_name` TEXT NOT NULL
- `last_name` TEXT NOT NULL
- `email` TEXT
- `phone` TEXT
- `preferred_channel` TEXT
- `notes` TEXT
- `created_at` TIMESTAMPTZ NOT NULL
- `updated_at` TIMESTAMPTZ NOT NULL

### client_tags

- `id` BIGSERIAL PK
- `tenant_id` BIGINT NOT NULL
- `name` TEXT NOT NULL
- `color` TEXT
- `description` TEXT
- `created_at` TIMESTAMPTZ NOT NULL

### client_tag_assignments

- `client_id` BIGINT NOT NULL
- `tag_id` BIGINT NOT NULL
- PK (`client_id`, `tag_id`)

### leads

- `id` BIGSERIAL PK
- `tenant_id` BIGINT NOT NULL
- `client_id` BIGINT NOT NULL
- `status` TEXT NOT NULL -- e.g. NEW, CONTACTED, PROPOSAL_SENT, BOOKED, LOST
- `source` TEXT
- `estimated_budget` NUMERIC(12,2)
- `notes` TEXT
- `created_at` TIMESTAMPTZ NOT NULL
- `updated_at` TIMESTAMPTZ NOT NULL

### events

- `id` BIGSERIAL PK
- `tenant_id` BIGINT NOT NULL
- `public_id` UUID NOT NULL
- `client_id` BIGINT NOT NULL
- `lead_id` BIGINT
- `type` TEXT NOT NULL -- e.g. WEDDING, ELOPEMENT, SESSION
- `date` DATE NOT NULL
- `start_time` TIME
- `end_time` TIME
- `location_name` TEXT
- `location_address` TEXT
- `status` TEXT NOT NULL -- e.g. INQUIRY, PENCILED, BOOKED, COMPLETED, CANCELLED
- `gallery_id` BIGINT -- FK to galleries.id (in the photo gallery app) when integrated
- `gallery_public_id` UUID -- external reference for cross-app linking
- `notes` TEXT
- `created_at` TIMESTAMPTZ NOT NULL
- `updated_at` TIMESTAMPTZ NOT NULL

### packages

- `id` BIGSERIAL PK
- `tenant_id` BIGINT NOT NULL
- `name` TEXT NOT NULL
- `description` TEXT
- `base_price` NUMERIC(12,2) NOT NULL
- `is_active` BOOLEAN NOT NULL DEFAULT TRUE
- `created_at` TIMESTAMPTZ NOT NULL
- `updated_at` TIMESTAMPTZ NOT NULL

### quotes

- `id` BIGSERIAL PK
- `tenant_id` BIGINT NOT NULL
- `public_id` UUID NOT NULL
- `lead_id` BIGINT
- `event_id` BIGINT
- `package_id` BIGINT
- `status` TEXT NOT NULL -- DRAFT, SENT, ACCEPTED, DECLINED, EXPIRED
- `valid_until` DATE
- `created_at` TIMESTAMPTZ NOT NULL
- `updated_at` TIMESTAMPTZ NOT NULL

### quote_line_items

- `id` BIGSERIAL PK
- `quote_id` BIGINT NOT NULL
- `description` TEXT NOT NULL
- `quantity` INTEGER NOT NULL DEFAULT 1
- `unit_price` NUMERIC(12,2) NOT NULL
- `sort_order` INTEGER NOT NULL DEFAULT 0

### invoices

- `id` BIGSERIAL PK
- `tenant_id` BIGINT NOT NULL
- `event_id` BIGINT NOT NULL
- `public_id` UUID NOT NULL
- `total_amount` NUMERIC(12,2) NOT NULL
- `amount_due` NUMERIC(12,2) NOT NULL
- `due_date` DATE
- `status` TEXT NOT NULL -- DRAFT, OPEN, PARTIALLY_PAID, PAID, OVERDUE
- `created_at` TIMESTAMPTZ NOT NULL
- `updated_at` TIMESTAMPTZ NOT NULL

### payments

- `id` BIGSERIAL PK
- `invoice_id` BIGINT NOT NULL
- `amount` NUMERIC(12,2) NOT NULL
- `method` TEXT NOT NULL -- CARD, CASH, CHECK, OTHER
- `received_at` TIMESTAMPTZ NOT NULL
- `notes` TEXT
- `created_at` TIMESTAMPTZ NOT NULL

### automation_rules

- `id` BIGSERIAL PK
- `tenant_id` BIGINT NOT NULL
- `name` TEXT NOT NULL
- `trigger_type` TEXT NOT NULL -- AFTER_EVENT, BEFORE_EVENT, AFTER_BOOKING, AFTER_PAYMENT
- `offset_days` INTEGER NOT NULL -- can be negative (for "before" triggers)
- `template_id` BIGINT NOT NULL -- FK to email_templates.id
- `is_active` BOOLEAN NOT NULL DEFAULT TRUE
- `created_at` TIMESTAMPTZ NOT NULL

### email_templates

- `id` BIGSERIAL PK
- `tenant_id` BIGINT NOT NULL
- `name` TEXT NOT NULL
- `subject` TEXT NOT NULL
- `body` TEXT NOT NULL -- may contain placeholders like {{client_first_name}}
- `description` TEXT
- `created_at` TIMESTAMPTZ NOT NULL
- `updated_at` TIMESTAMPTZ NOT NULL

### activity_log

- `id` BIGSERIAL PK
- `tenant_id` BIGINT NOT NULL
- `user_id` BIGINT
- `activity_type` TEXT NOT NULL
- `entity_type` TEXT NOT NULL
- `entity_id` BIGINT NOT NULL
- `details` JSONB
- `created_at` TIMESTAMPTZ NOT NULL

You can refine column types, indexes, and foreign keys as you integrate with
the existing gallery database.
