--liquibase formatted sql

--changeset severin:1
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
    username VARCHAR(64) NOT NULL UNIQUE,
    phone VARCHAR(64),
    city VARCHAR(64),
    email VARCHAR(64) NOT NULL,
    role VARCHAR(32) NOT NULL
    );