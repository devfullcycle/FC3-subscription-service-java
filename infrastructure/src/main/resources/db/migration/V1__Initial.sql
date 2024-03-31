CREATE TABLE accounts
(
    id                 VARCHAR(32)  NOT NULL PRIMARY KEY,
    version            SMALLINT     NOT NULL DEFAULT 0,
    iam_user_id        VARCHAR(255) NOT NULL UNIQUE,
    email              VARCHAR(255) NOT NULL UNIQUE,
    firstname          VARCHAR(255) NOT NULL,
    lastname           VARCHAR(255) NOT NULL,
    address_zip_code   VARCHAR(255),
    address_number     VARCHAR(255),
    address_complement VARCHAR(255),
    address_country    VARCHAR(255),
    document_number    VARCHAR(255) NOT NULL,
    document_type      VARCHAR(255) NOT NULL
);

CREATE TABLE events
(
    event_id       INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    processed      BOOLEAN      NOT NULL DEFAULT FALSE,
    aggregate_type VARCHAR(32)  NOT NULL,
    aggregate_id   VARCHAR(32)  NOT NULL,
    event_type     VARCHAR(255) NOT NULL,
    event_date     TIMESTAMP(6) NOT NULL,
    event_data     JSON         NOT NULL
);

CREATE INDEX idx_events_aggregates ON events (aggregate_id, aggregate_type);

CREATE TABLE plans
(
    id          VARCHAR(32)    NOT NULL PRIMARY KEY,
    version     SMALLINT       NOT NULL DEFAULT 0,
    name        VARCHAR(255)   NOT NULL,
    description VARCHAR(4000)  NOT NULL,
    group_id    VARCHAR(255)   NOT NULL,
    active      BOOLEAN        NOT NULL DEFAULT TRUE,
    currency    VARCHAR(3)     NOT NULL,
    amount      DECIMAL(15, 2) NOT NULL
);

CREATE TABLE subscriptions
(
    id                  VARCHAR(32)  NOT NULL PRIMARY KEY,
    version             SMALLINT     NOT NULL DEFAULT 0,
    account_id          VARCHAR(32)  NOT NULL,
    plan_id             VARCHAR(32)  NOT NULL,
    status              VARCHAR(32)  NOT NULL,
    created_at          TIMESTAMP(6) NOT NULL,
    updated_at          TIMESTAMP(6) NOT NULL,
    due_date            DATE         NOT NULL,
    last_renew_dt       TIMESTAMP(6) NOT NULL,
    last_transaction_id VARCHAR(32)  NOT NULL
);

CREATE INDEX idx_subscriptions_account ON subscriptions (account_id);
CREATE INDEX idx_subscriptions_due_date ON subscriptions (due_date);