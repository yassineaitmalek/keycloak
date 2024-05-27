package com.test.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.test.config.properties.KeycloakProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class KeycloakConfiguration {

  private final KeycloakProperties keycloakProperties;

  @Bean
  public Keycloak keycloak() {
    return KeycloakBuilder.builder()
        .realm(keycloakProperties.getRealm())
        .clientId(keycloakProperties.getResource())
        .clientSecret(keycloakProperties.getSecret())
        .serverUrl(keycloakProperties.getAuthServerUrl())
        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        .build();

  }

  @Bean(value = "keycloackSpringConfigResolver2")
  public KeycloakSpringBootConfigResolver getKeycloakSpringBootConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
  }
}
