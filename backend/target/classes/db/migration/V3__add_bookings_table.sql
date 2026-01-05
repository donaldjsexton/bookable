-- Add bookings table to the booking_crm schema.
CREATE TABLE IF NOT EXISTS booking_crm.bookings (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    client_id BIGINT NOT NULL,
    event_id BIGINT,
    quote_id BIGINT,
    invoice_id BIGINT,
    state TEXT NOT NULL,
    consult_date DATE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_bookings_tenant ON booking_crm.bookings(tenant_id);
CREATE INDEX IF NOT EXISTS idx_bookings_client ON booking_crm.bookings(client_id);

DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_bookings_client') THEN
    EXECUTE 'ALTER TABLE booking_crm.bookings ADD CONSTRAINT fk_booking_crm_bookings_client FOREIGN KEY (client_id) REFERENCES booking_crm.clients(id) ON DELETE CASCADE';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_bookings_event') THEN
    EXECUTE 'ALTER TABLE booking_crm.bookings ADD CONSTRAINT fk_booking_crm_bookings_event FOREIGN KEY (event_id) REFERENCES booking_crm.events(id) ON DELETE SET NULL';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_bookings_quote') THEN
    EXECUTE 'ALTER TABLE booking_crm.bookings ADD CONSTRAINT fk_booking_crm_bookings_quote FOREIGN KEY (quote_id) REFERENCES booking_crm.quotes(id) ON DELETE SET NULL';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_bookings_invoice') THEN
    EXECUTE 'ALTER TABLE booking_crm.bookings ADD CONSTRAINT fk_booking_crm_bookings_invoice FOREIGN KEY (invoice_id) REFERENCES booking_crm.invoices(id) ON DELETE SET NULL';
  END IF;
END
$$;

DO $$
BEGIN
  IF to_regclass('public.tenants') IS NOT NULL THEN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_booking_crm_bookings_tenant') THEN
      EXECUTE 'ALTER TABLE booking_crm.bookings ADD CONSTRAINT fk_booking_crm_bookings_tenant FOREIGN KEY (tenant_id) REFERENCES public.tenants(id) ON DELETE CASCADE';
    END IF;
  END IF;
END
$$;
