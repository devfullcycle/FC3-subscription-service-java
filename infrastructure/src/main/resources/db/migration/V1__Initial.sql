CREATE TABLE accounts (
    id CHAR(32) NOT NULL PRIMARY KEY,
    version SMALLINT NOT NULL DEFAULT 0,
    idp_user_id VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    document_number VARCHAR(255) NOT NULL,
    document_type VARCHAR(255) NOT NULL,
    address_zip_code VARCHAR(255),
    address_number VARCHAR(255),
    address_complement VARCHAR(255),
    address_country VARCHAR(255)
);

CREATE TABLE events (
    event_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    processed BOOLEAN NOT NULL DEFAULT FALSE,
    aggregate_type VARCHAR(32) NOT NULL,
    aggregate_id VARCHAR(32) NOT NULL,
    event_type VARCHAR(500) NOT NULL,
    event_date TIMESTAMP(6) NOT NULL,
    event_data JSON
);

CREATE INDEX idx_events_aggregates ON events (aggregate_id);