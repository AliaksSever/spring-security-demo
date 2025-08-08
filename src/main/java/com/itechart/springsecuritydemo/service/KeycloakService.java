package com.itechart.springsecuritydemo.service;

import com.itechart.springsecuritydemo.dto.UpdateUserRequest;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class KeycloakService {
    private final Keycloak keycloak;

    private final String realm = "proselyte";

    public void updateKeycloakUser(UUID uuid, UpdateUserRequest updateUserRequest){
        UserResource userResource = keycloak.realm(realm).users().get(String.valueOf(uuid));
        UserRepresentation user = userResource.toRepresentation();
        user.setUsername(updateUserRequest.username());
        userResource.update(user);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(updateUserRequest.newPassword());
        credentialRepresentation.setTemporary(false);
        userResource.resetPassword(credentialRepresentation);

        userResource.logout();
        System.out.println("Username and password updated, user logged out: {}" + uuid);
    }
}
