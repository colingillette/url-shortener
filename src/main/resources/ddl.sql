-- Step 1: Run Site items first
CREATE TABLE site (
                      id UUID PRIMARY KEY,
                      short_url VARCHAR(255) NOT NULL UNIQUE,
                      long_url VARCHAR(2048) NOT NULL,
                      create_email VARCHAR(255) NOT NULL,
                      create_utc TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      revision_utc TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE EXTENSION IF NOT EXISTS pgcrypto;

ALTER TABLE site
ALTER COLUMN id SET DEFAULT gen_random_uuid();

CREATE OR REPLACE FUNCTION update_revision_utc()
RETURNS TRIGGER AS $$
BEGIN
    NEW.revision_utc = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER site_revision_trigger
BEFORE UPDATE ON site
FOR EACH ROW
EXECUTE FUNCTION update_revision_utc();

-- Step 2: Run Hit items second
CREATE TABLE hit (
     correlation_id UUID PRIMARY KEY,
     site_id UUID NOT NULL,
     hit_utc TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

     CONSTRAINT fk_hit_site
     FOREIGN KEY (site_id)
     REFERENCES site(id)
     ON DELETE CASCADE
);

CREATE EXTENSION IF NOT EXISTS pgcrypto;

ALTER TABLE hit
ALTER COLUMN correlation_id SET DEFAULT gen_random_uuid();

CREATE INDEX idx_hit_site_id ON hit(site_id);