package com.test.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.test.config.properties.KeycloakProperties;
import com.test.exception.config.ApiException;
import com.test.models.local.User;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {

  private final KeycloakProperties KeycloakProperties;

  private final KeycloakSecurityContext keycloakSecurityContext;

  private final Keycloak keycloak;

  public AccessToken getAccessToken() {

    return keycloakSecurityContext.getToken();

  }

  public Set<String> getCurrentUserRoles() {

    return getAccessToken().getRealmAccess().getRoles();

  }

  public String getCurrentUserId() {

    return getAccessToken().getSubject();
  }

  public UserRepresentation getCurrentUser() {

    return getUserRepresentation(getCurrentUserId());
  }

  public UsersResource getUsersResources() {
    return keycloak.realm(KeycloakProperties.getRealm()).users();
  }

  public UserResource getUserResource(String userId) {
    return getUsersResources().get(userId);
  }

  public void deleteUser(String userId) {
    getUsersResources().delete(userId);
  }

  public UserRepresentation updateUser(String userId, User user) {
    UserResource userResource = getUserResource(userId);
    UserRepresentation userRepresentation = updateUserRepresentation(user, userResource.toRepresentation());
    userResource.update(userRepresentation);
    return userRepresentation;
  }

  public UserRepresentation getUserRepresentation(String userId) {
    return getUserResource(userId).toRepresentation();
  }

  public UserRepresentation updateUserRepresentation(User user, UserRepresentation userRepresentation) {

    userRepresentation.setUsername(user.getUsername());
    userRepresentation.setFirstName(user.getFirstName());
    userRepresentation.setLastName(user.getLastName().toUpperCase());
    userRepresentation.setEmail(user.getEmail());
    userRepresentation.setEmailVerified(user.isEmailVerified());
    userRepresentation.setRealmRoles(user.getRoles());

    return userRepresentation;
  }

  public UserRepresentation prepareUserRepresentation(User user) {
    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setEnabled(true);
    userRepresentation.setUsername(user.getUsername());
    userRepresentation.setFirstName(user.getFirstName());
    userRepresentation.setLastName(user.getLastName().toUpperCase());
    userRepresentation.setEmail(user.getEmail());
    userRepresentation.setEmailVerified(user.isEmailVerified());
    userRepresentation.setRealmRoles(user.getRoles());
    userRepresentation.setCredentials(Collections.singletonList(kcPassword(user.getPassword(), false)));
    return userRepresentation;
  }

  public CredentialRepresentation kcPassword(String password, boolean temporary) {

    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
    credentialRepresentation.setTemporary(temporary);
    credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
    credentialRepresentation.setValue(password);
    return credentialRepresentation;

  }

  public UserRepresentation createUser(User user) {

    return Optional.of(getUsersResources().create(prepareUserRepresentation(user)))
        .filter(response -> response.getStatusInfo().equals(Response.Status.CREATED))
        .map(response -> response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1"))
        .map(this::getUserRepresentation)
        .orElseThrow(() -> new ApiException("user was not created"));

  }

  public RolesResource getRolesResources() {
    return keycloak.realm(KeycloakProperties.getRealm()).roles();
  }

  public List<RoleRepresentation> getAllRoles() {
    return getRolesResources().list();

  }

  public RoleRepresentation createRoleRepresentation(String role, String description) {

    RoleRepresentation roleRepresentation = new RoleRepresentation();
    roleRepresentation.setName(role);
    roleRepresentation.setDescription(description);
    getRolesResources().create(roleRepresentation);
    return roleRepresentation;

  }

  public List<RoleRepresentation> getByUserId(String userId) {

    return keycloak
        .realm(KeycloakProperties.getRealm())
        .users()
        .get(userId)
        .roles()
        .realmLevel()
        .listAll();

  }

}
