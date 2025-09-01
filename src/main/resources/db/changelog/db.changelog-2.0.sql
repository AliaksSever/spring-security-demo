--liquibase formatted sql

--changeset severin:1
INSERT INTO users (username, email)
VALUES
    ('user1', 'user1@gmail.com'),
    ('admin1', 'admin1@gmail.com');

