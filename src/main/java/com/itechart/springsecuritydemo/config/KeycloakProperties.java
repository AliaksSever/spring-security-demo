package com.itechart.springsecuritydemo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "keycloak")
@Getter
@Setter
public class KeycloakProperties {

    private String realm;
    private String serverUrl;
    private UserSyncClient userSyncClient;

    @Getter
    @Setter
    public static class UserSyncClient {
        private String clientId;
        private String clientSecret;
    }

}
