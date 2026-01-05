-- Initial schema for Booking + Client CRM
-- Assumes a multi-tenant environment with an existing `tenants` table.
-- Add FOREIGN KEY constraints to `tenants(id)` and gallery tables when integrated.

CREATE TABLE clients (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    public_id UUID NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT,
    phone TEXT,
    preferred_channel TEXT,
    notes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE client_tags (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    name TEXT NOT NULL,
    color TEXT,
    description TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE client_tag_assignments (
    client_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (client_id, tag_id)
);

CREATE TABLE leads (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    client_id BIGINT NOT NULL,
    status TEXT NOT NULL,
    source TEXT,
    estimated_budget NUMERIC(12,2),
    notes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    public_id UUID NOT NULL,
    client_id BIGINT NOT NULL,
    lead_id BIGINT,
    type TEXT NOT NULL,
    date DATE NOT NULL,
    start_time TIME,
    end_time TIME,
    location_name TEXT,
    location_address TEXT,
    status TEXT NOT NULL,
    gallery_id BIGINT,
    gallery_public_id UUID,
    notes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE packages (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    name TEXT NOT NULL,
    description TEXT,
    base_price NUMERIC(12,2) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE quotes (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    public_id UUID NOT NULL,
    lead_id BIGINT,
    event_id BIGINT,
    package_id BIGINT,
    status TEXT NOT NULL,
    valid_until DATE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE quote_line_items (
    id BIGSERIAL PRIMARY KEY,
    quote_id BIGINT NOT NULL,
    description TEXT NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_price NUMERIC(12,2) NOT NULL,
    sort_order INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE invoices (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    public_id UUID NOT NULL,
    total_amount NUMERIC(12,2) NOT NULL,
    amount_due NUMERIC(12,2) NOT NULL,
    due_date DATE,
    status TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    invoice_id BIGINT NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    method TEXT NOT NULL,
    received_at TIMESTAMPTZ NOT NULL,
    notes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE automation_rules (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    name TEXT NOT NULL,
    trigger_type TEXT NOT NULL,
    offset_days INTEGER NOT NULL,
    template_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE email_templates (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    name TEXT NOT NULL,
    subject TEXT NOT NULL,
    body TEXT NOT NULL,
    description TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE activity_log (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    user_id BIGINT,
    activity_type TEXT NOT NULL,
    entity_type TEXT NOT NULL,
    entity_id BIGINT NOT NULL,
    details JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Basic indexes to help with typical queries
CREATE INDEX idx_clients_tenant ON clients(tenant_id);
CREATE INDEX idx_leads_tenant ON leads(tenant_id);
CREATE INDEX idx_events_tenant ON events(tenant_id);
CREATE INDEX idx_invoices_tenant ON invoices(tenant_id);
CREATE INDEX idx_payments_invoice ON payments(invoice_id);
CREATE INDEX idx_quotes_tenant ON quotes(tenant_id);
CREATE INDEX idx_activity_log_tenant ON activity_log(tenant_id);
