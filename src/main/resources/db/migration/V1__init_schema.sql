CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
       username VARCHAR(50) NOT NULL UNIQUE,
       email VARCHAR(255) NOT NULL UNIQUE,
       password_hash VARCHAR(255) NOT NULL,
       is_active BOOLEAN NOT NULL DEFAULT TRUE,
       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE campaigns (
       id BIGSERIAL PRIMARY KEY,
       name VARCHAR(255) NOT NULL,
       description TEXT,
       owner_id UUID NOT NULL,
       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

       CONSTRAINT fk_campaign_owner FOREIGN KEY (owner_id)
           REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE characters (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    backstory TEXT,
    campaign_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_character_campaign_id FOREIGN KEY (campaign_id)
        REFERENCES campaigns(id) ON DELETE CASCADE

);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_campaigns_owner ON campaigns(owner_id);
CREATE INDEX idx_characters_campaign ON characters(campaign_id);

CREATE OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_user_modtime
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_modified_column();

CREATE TRIGGER update_character_modtime
    BEFORE UPDATE ON characters
    FOR EACH ROW
    EXECUTE FUNCTION update_modified_column();