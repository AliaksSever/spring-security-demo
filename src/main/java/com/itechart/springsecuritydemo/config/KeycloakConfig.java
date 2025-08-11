package com.itechart.springsecuritydemo.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeycloakConfig {

    private final KeycloakProperties keycloakProperties;

    @Bean
    public Keycloak keycloak() {

        KeycloakProperties.UserSyncClient userSyncClient = keycloakProperties.getUserSyncClient();

        return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getServerUrl())
                .realm(keycloakProperties.getRealm())
                .clientId("postman-client2")
                .username("adminuser")
                .password("admin123")
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }
}
