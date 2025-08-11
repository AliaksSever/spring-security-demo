package com.itechart.springsecuritydemo.service;

import com.itechart.springsecuritydemo.config.KeycloakProperties;
import com.itechart.springsecuritydemo.dto.UpdateUserRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class KeycloakService {

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;

    public void updateKeycloakUser(UUID uuid, UpdateUserRequest updateUserRequest){
        UserResource userResource = keycloak.realm(keycloakProperties.getRealm()).users().get(String.valueOf(uuid));
        UserRepresentation user = userResource.toRepresentation();
        user.setEmail(updateUserRequest.email());
        userResource.update(user);

        userResource.logout();
        System.out.println("Username and password updated, user logged out: {}" + uuid);
    }
}
