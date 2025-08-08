package com.itechart.springsecuritydemo.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:9090")
                .realm("proselyte")
                .clientId("admin-client")
                .clientSecret("9Mq6a6BHzUQBcuNQaPHgwMa8XkO4GZmB")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }
}
