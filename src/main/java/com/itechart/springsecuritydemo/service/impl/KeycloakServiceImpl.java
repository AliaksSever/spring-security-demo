package com.itechart.springsecuritydemo.service.impl;

import com.itechart.springsecuritydemo.config.KeycloakProperties;
import com.itechart.profileserviceapi.dto.UpdateUserRequest;
import com.itechart.springsecuritydemo.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class KeycloakServiceImpl implements KeycloakService {

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;

    public void updateKeycloakUser(UUID uuid, UpdateUserRequest updateUserRequest){
        UserResource userResource = keycloak.realm(keycloakProperties.getRealm()).users().get(String.valueOf(uuid));
        UserRepresentation user = userResource.toRepresentation();
        user.setEmail(updateUserRequest.email());
        userResource.update(user);

        System.out.println("Username and password updated, user logged out: {}" + uuid);
    }

    public void deleteKeycloakUser(UUID uuid){
        UserResource userResource = keycloak.realm(keycloakProperties.getRealm()).users().get(String.valueOf(uuid));
        userResource.remove();
    }
    public void updateUserRole(UUID uuid, String newRoleName) {
        UserResource userResource = keycloak.realm(keycloakProperties.getRealm())
                .users().get(uuid.toString());
        RoleRepresentation role = keycloak.realm(keycloakProperties.getRealm())
                .roles().get(newRoleName).toRepresentation();
        userResource.roles().realmLevel().add(List.of(role));
    }

    @Override
    public void deleteUserRole(UUID uuid, String role) {
        UserResource userResource = keycloak.realm(keycloakProperties.getRealm())
                .users().get(uuid.toString());
        RoleRepresentation currRole = keycloak.realm(keycloakProperties.getRealm())
                .roles().get(role).toRepresentation();
        userResource.roles().realmLevel().remove(List.of(currRole));
    }
}
