CREATE TABLE races (
        id BIGSERIAL PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        description TEXT,
        campaign_id BIGINT NOT NULL REFERENCES campaigns(id) ON DELETE CASCADE,
        created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE classes (
        id BIGSERIAL PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        description TEXT,
        campaign_id BIGINT NOT NULL REFERENCES campaigns(id) ON DELETE CASCADE,
        created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE subclasses (
        id BIGSERIAL PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        description TEXT,
        class_id BIGINT NOT NULL REFERENCES classes(id) ON DELETE CASCADE,
        campaign_id BIGINT NOT NULL REFERENCES campaigns(id) ON DELETE CASCADE,
        created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE characters ADD COLUMN race_id BIGINT REFERENCES races(id) ON DELETE SET NULL;
ALTER TABLE characters ADD COLUMN class_id BIGINT REFERENCES classes(id) ON DELETE SET NULL;
ALTER TABLE characters ADD COLUMN subclass_id BIGINT REFERENCES subclasses(id) ON DELETE SET NULL;