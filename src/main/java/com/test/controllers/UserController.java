package com.test.controllers;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.controllers.config.AbstractController;
import com.test.controllers.config.ApiDataResponse;
import com.test.models.local.User;
import com.test.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController implements AbstractController {

  private final UserService userService;

  @PostMapping("/user")
  public ResponseEntity<ApiDataResponse<UserRepresentation>> createUser(@RequestBody User user) {

    return create(() -> userService.createUser(user));
  }

  @DeleteMapping("/user/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable String userId) {

    return noContent(() -> userService.deleteUser(userId));
  }

  @GetMapping("/currentUser")
  public ResponseEntity<ApiDataResponse<UserRepresentation>> getCurrentUser() {
    return ok(userService::getCurrentUser);

  }
}
