--liquibase formatted sql

--changeset severin:1
INSERT INTO users (username, email, password, role)
VALUES
    ('user1', 'user1@gmail.com', '{noop}123', 'USER'),
    ('admin1', 'admin1@gmail.com', '123', 'ADMIN');