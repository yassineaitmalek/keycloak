package com.test.config.properties;

import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {

  private String realm;

  private String authServerUrl;

  private String resource;

  private boolean bearerOnly;

  private final CredentialsKeycloakProperties credentialsKeycloakProperties;

  public String getSecret() {
    return Optional.ofNullable(credentialsKeycloakProperties)
        .map(CredentialsKeycloakProperties::getSecret)
        .orElse(null);
  }

}

@Data
@Configuration
@ConfigurationProperties(prefix = "keycloak.credentials")
class CredentialsKeycloakProperties {

  private String secret;

}
