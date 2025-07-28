--liquibase formatted sql

--changeset severin:1
INSERT INTO users (username, email, password, role)
VALUES
    ('user1', 'user1@gmail.com', '$2a$10$gZJw6v8XxK9uYzQeL1sY0uYzQeL1sY0uYzQeL1sY0uYzQeL1sY0u', 'USER'),
    ('admin1', 'admin1@gmail.com', '$2a$10$gZJw6v8XxK9uYzQeL1sY0uYzQeL1sY0uYzQeL1sY0uYzQeL1sY0u', 'ADMIN');