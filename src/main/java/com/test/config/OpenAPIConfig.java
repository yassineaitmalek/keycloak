package com.test.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.test.config.properties.KeycloakProperties;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@OpenAPIDefinition(info = @Info(title = "TEST", version = "1.0", description = "TEST"))
public class OpenAPIConfig {

  private final KeycloakProperties keycloakProperties;

  private static final String SECURITY_SCHEME_NAME = "bearerAuth";

  private static final String SECURITY_SCHEME_NAME_KC = "keyclaokAuth";

  private static final String SECURITY_SCHEME = "bearer";

  private static final String SECURITY_BEARER_FORMAT = "bearer";

  @Bean
  public OpenAPI openAPI() {

    return new OpenAPI()
        .servers(servers())
        .addSecurityItem(securityRequirement())
        .components(components());

  }

  public List<Server> servers() {

    Server server = new Server();
    server.setUrl("/");
    return Arrays.asList(server);

  }

  private SecurityRequirement securityRequirement() {
    return new SecurityRequirement()
        .addList(SECURITY_SCHEME_NAME);

  }

  private Components components() {
    return new Components()
        .addSecuritySchemes(SECURITY_SCHEME_NAME, securitySchemeKeycloak());

  }

  private SecurityScheme securitySchemeNormal() {

    return new SecurityScheme()
        .name(SECURITY_SCHEME_NAME)
        .type(SecurityScheme.Type.HTTP)
        .scheme(SECURITY_SCHEME)
        .bearerFormat(SECURITY_BEARER_FORMAT);

  }

  private SecurityScheme securitySchemeKeycloak() {

    return new SecurityScheme()
        .name(SECURITY_SCHEME_NAME_KC)
        .type(SecurityScheme.Type.OPENIDCONNECT)
        .openIdConnectUrl(keycloakProperties.getAuthServerUrl() + "/realms/testRealm/.well-known/openid-configuration")
        .scheme(SECURITY_SCHEME)
        .in(SecurityScheme.In.HEADER);

  }

}
