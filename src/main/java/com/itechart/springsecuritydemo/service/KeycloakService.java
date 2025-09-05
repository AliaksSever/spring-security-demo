package com.itechart.springsecuritydemo.service;

import com.itechart.profileserviceapi.dto.UpdateUserRequest;

import java.util.UUID;

public interface KeycloakService {
    void updateKeycloakUser(UUID uuid, UpdateUserRequest updateUserRequest);
    void deleteKeycloakUser(UUID uuid);
    void updateUserRole(UUID uuid, String newRoleName);
    void deleteUserRole(UUID uuid, String role);
}
