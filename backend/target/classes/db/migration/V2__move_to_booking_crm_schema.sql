-- Keep Booking CRM tables isolated in their own schema for clean separation now,
-- while still enabling seamless later integration with the Photo Gallery app.

CREATE SCHEMA IF NOT EXISTS booking_crm;

-- If V1 created tables in public, move them into booking_crm (safe to run on a fresh DB too).
DO $$
BEGIN
  IF to_regclass('public.clients') IS NOT NULL THEN
    EXECUTE 'ALTER TABLE public.clients SET SCHEMA booking_crm';
  END IF;
  IF to_regclass('public.client_tags') IS NOT NULL THEN
    EXECUTE 'ALTER TABLE public.client_tags SET SCHEMA booking_crm';
  END IF;
  IF to_regclass('public.client_tag_assignments') IS NOT NULL THEN
    EXECUTE 'ALTER TABLE public.client_tag_assignments SET SCHEMA booking_crm';
  END IF;
  IF to_regclass('public.leads') IS NOT NULL THEN
    EXECUTE 'ALTER TABLE public.leads SET SCHEMA booking_crm';
  END IF;
  IF to_regclass('public.events') IS NOT NULL THEN
    EXECUTE 'ALTER TABLE public.events SET SCHEMA booking_crm';
  END IF;
  IF to_regclass('public.packages') IS NOT NULL THEN
    EXECUTE 'ALTER TABLE public.packages SET SCHEMA booking_crm';
  END IF;
  IF to_regclass('public.quotes') IS NOT NULL THEN
    EXECUTE 'ALTER TABLE public.quotes SET SCHEMA booking_crm';
  END IF;
  IF to_regclass('public.quote_line_items') IS NOT NULL THEN
    EXECUTE 'ALTER TABLE public.quote_line_items SET SCHEMA booking_crm';
  END IF;
  IF to_regclass('public.invoices') IS NOT NULL THEN
    EXECUTE 'ALTER TABLE public.invoices SET SCHEMA booking_crm';
  END IF;
  IF to_regclass('public.payments') IS NOT NULL THEN
    EXECUTE 'ALTER TABLE public.payments SET SCHEMA booking_crm';
  END IF;
  IF to_regclass('public.automation_rules') IS NOT NULL THEN
    EXECUTE 'ALTER TABLE public.automation_rules SET SCHEMA booking_crm';
  END IF;
  IF to_regclass('public.email_templates') IS NOT NULL THEN
    EXECUTE 'ALTER TABLE public.email_templates SET SCHEMA booking_crm';
  END IF;
  IF to_regclass('public.activity_log') IS NOT NULL THEN
    EXECUTE 'ALTER TABLE public.activity_log SET SCHEMA booking_crm';
  END IF;
END
$$;

-- Move any V1-generated sequences into booking_crm too (optional, but keeps the schema tidy).
DO $$
BEGIN
  IF to_regclass('public.clients_id_seq') IS NOT NULL THEN EXECUTE 'ALTER SEQUENCE public.clients_id_seq SET SCHEMA booking_crm'; END IF;
  IF to_regclass('public.client_tags_id_seq') IS NOT NULL THEN EXECUTE 'ALTER SEQUENCE public.client_tags_id_seq SET SCHEMA booking_crm'; END IF;
  IF to_regclass('public.leads_id_seq') IS NOT NULL THEN EXECUTE 'ALTER SEQUENCE public.leads_id_seq SET SCHEMA booking_crm'; END IF;
  IF to_regclass('public.events_id_seq') IS NOT NULL THEN EXECUTE 'ALTER SEQUENCE public.events_id_seq SET SCHEMA booking_crm'; END IF;
  IF to_regclass('public.packages_id_seq') IS NOT NULL THEN EXECUTE 'ALTER SEQUENCE public.packages_id_seq SET SCHEMA booking_crm'; END IF;
  IF to_regclass('public.quotes_id_seq') IS NOT NULL THEN EXECUTE 'ALTER SEQUENCE public.quotes_id_seq SET SCHEMA booking_crm'; END IF;
  IF to_regclass('public.quote_line_items_id_seq') IS NOT NULL THEN EXECUTE 'ALTER SEQUENCE public.quote_line_items_id_seq SET SCHEMA booking_crm'; END IF;
  IF to_regclass('public.invoices_id_seq') IS NOT NULL THEN EXECUTE 'ALTER SEQUENCE public.invoices_id_seq SET SCHEMA booking_crm'; END IF;
  IF to_regclass('public.payments_id_seq') IS NOT NULL THEN EXECUTE 'ALTER SEQUENCE public.payments_id_seq SET SCHEMA booking_crm'; END IF;
  IF to_regclass('public.automation_rules_id_seq') IS NOT NULL THEN EXECUTE 'ALTER SEQUENCE public.automation_rules_id_seq SET SCHEMA booking_crm'; END IF;
  IF to_regclass('public.email_templates_id_seq') IS NOT NULL THEN EXECUTE 'ALTER SEQUENCE public.email_templates_id_seq SET SCHEMA booking_crm'; END IF;
  IF to_regclass('public.activity_log_id_seq') IS NOT NULL THEN EXECUTE 'ALTER SEQUENCE public.activity_log_id_seq SET SCHEMA booking_crm'; END IF;
END
$$;

-- Add internal foreign keys (keeps CRM data consistent regardless of integration).
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_client_tag_assignments_client') THEN
    EXECUTE 'ALTER TABLE booking_crm.client_tag_assignments ADD CONSTRAINT fk_booking_crm_client_tag_assignments_client FOREIGN KEY (client_id) REFERENCES booking_crm.clients(id) ON DELETE CASCADE';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_client_tag_assignments_tag') THEN
    EXECUTE 'ALTER TABLE booking_crm.client_tag_assignments ADD CONSTRAINT fk_booking_crm_client_tag_assignments_tag FOREIGN KEY (tag_id) REFERENCES booking_crm.client_tags(id) ON DELETE CASCADE';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_leads_client') THEN
    EXECUTE 'ALTER TABLE booking_crm.leads ADD CONSTRAINT fk_booking_crm_leads_client FOREIGN KEY (client_id) REFERENCES booking_crm.clients(id) ON DELETE CASCADE';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_events_client') THEN
    EXECUTE 'ALTER TABLE booking_crm.events ADD CONSTRAINT fk_booking_crm_events_client FOREIGN KEY (client_id) REFERENCES booking_crm.clients(id) ON DELETE CASCADE';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_events_lead') THEN
    EXECUTE 'ALTER TABLE booking_crm.events ADD CONSTRAINT fk_booking_crm_events_lead FOREIGN KEY (lead_id) REFERENCES booking_crm.leads(id) ON DELETE SET NULL';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_quotes_event') THEN
    EXECUTE 'ALTER TABLE booking_crm.quotes ADD CONSTRAINT fk_booking_crm_quotes_event FOREIGN KEY (event_id) REFERENCES booking_crm.events(id) ON DELETE SET NULL';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_quotes_lead') THEN
    EXECUTE 'ALTER TABLE booking_crm.quotes ADD CONSTRAINT fk_booking_crm_quotes_lead FOREIGN KEY (lead_id) REFERENCES booking_crm.leads(id) ON DELETE SET NULL';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_quotes_package') THEN
    EXECUTE 'ALTER TABLE booking_crm.quotes ADD CONSTRAINT fk_booking_crm_quotes_package FOREIGN KEY (package_id) REFERENCES booking_crm.packages(id) ON DELETE SET NULL';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_quote_line_items_quote') THEN
    EXECUTE 'ALTER TABLE booking_crm.quote_line_items ADD CONSTRAINT fk_booking_crm_quote_line_items_quote FOREIGN KEY (quote_id) REFERENCES booking_crm.quotes(id) ON DELETE CASCADE';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_invoices_event') THEN
    EXECUTE 'ALTER TABLE booking_crm.invoices ADD CONSTRAINT fk_booking_crm_invoices_event FOREIGN KEY (event_id) REFERENCES booking_crm.events(id) ON DELETE CASCADE';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_payments_invoice') THEN
    EXECUTE 'ALTER TABLE booking_crm.payments ADD CONSTRAINT fk_booking_crm_payments_invoice FOREIGN KEY (invoice_id) REFERENCES booking_crm.invoices(id) ON DELETE CASCADE';
  END IF;
END
$$;

-- Add cross-app foreign keys only when the Photo Gallery tables exist (enables "seamless later").
DO $$
BEGIN
  IF to_regclass('public.tenants') IS NOT NULL THEN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_clients_tenant') THEN
      EXECUTE 'ALTER TABLE booking_crm.clients ADD CONSTRAINT fk_booking_crm_clients_tenant FOREIGN KEY (tenant_id) REFERENCES public.tenants(id) ON DELETE CASCADE';
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_client_tags_tenant') THEN
      EXECUTE 'ALTER TABLE booking_crm.client_tags ADD CONSTRAINT fk_booking_crm_client_tags_tenant FOREIGN KEY (tenant_id) REFERENCES public.tenants(id) ON DELETE CASCADE';
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_leads_tenant') THEN
      EXECUTE 'ALTER TABLE booking_crm.leads ADD CONSTRAINT fk_booking_crm_leads_tenant FOREIGN KEY (tenant_id) REFERENCES public.tenants(id) ON DELETE CASCADE';
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_events_tenant') THEN
      EXECUTE 'ALTER TABLE booking_crm.events ADD CONSTRAINT fk_booking_crm_events_tenant FOREIGN KEY (tenant_id) REFERENCES public.tenants(id) ON DELETE CASCADE';
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_packages_tenant') THEN
      EXECUTE 'ALTER TABLE booking_crm.packages ADD CONSTRAINT fk_booking_crm_packages_tenant FOREIGN KEY (tenant_id) REFERENCES public.tenants(id) ON DELETE CASCADE';
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_quotes_tenant') THEN
      EXECUTE 'ALTER TABLE booking_crm.quotes ADD CONSTRAINT fk_booking_crm_quotes_tenant FOREIGN KEY (tenant_id) REFERENCES public.tenants(id) ON DELETE CASCADE';
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_invoices_tenant') THEN
      EXECUTE 'ALTER TABLE booking_crm.invoices ADD CONSTRAINT fk_booking_crm_invoices_tenant FOREIGN KEY (tenant_id) REFERENCES public.tenants(id) ON DELETE CASCADE';
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_automation_rules_tenant') THEN
      EXECUTE 'ALTER TABLE booking_crm.automation_rules ADD CONSTRAINT fk_booking_crm_automation_rules_tenant FOREIGN KEY (tenant_id) REFERENCES public.tenants(id) ON DELETE CASCADE';
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_email_templates_tenant') THEN
      EXECUTE 'ALTER TABLE booking_crm.email_templates ADD CONSTRAINT fk_booking_crm_email_templates_tenant FOREIGN KEY (tenant_id) REFERENCES public.tenants(id) ON DELETE CASCADE';
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_activity_log_tenant') THEN
      EXECUTE 'ALTER TABLE booking_crm.activity_log ADD CONSTRAINT fk_booking_crm_activity_log_tenant FOREIGN KEY (tenant_id) REFERENCES public.tenants(id) ON DELETE CASCADE';
    END IF;
  END IF;

  IF to_regclass('public.galleries') IS NOT NULL THEN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_events_gallery') THEN
      EXECUTE 'ALTER TABLE booking_crm.events ADD CONSTRAINT fk_booking_crm_events_gallery FOREIGN KEY (gallery_id) REFERENCES public.galleries(id) ON DELETE SET NULL';
    END IF;
  END IF;
END
$$;
