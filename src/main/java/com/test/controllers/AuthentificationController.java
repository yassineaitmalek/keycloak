package com.test.controllers;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.controllers.config.AbstractController;
import com.test.controllers.config.ApiDataResponse;
import com.test.dto.RefreshTokenDTO;
import com.test.dto.SignInDTO;
import com.test.services.AuthentificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthentificationController implements AbstractController {

  private final AuthentificationService authentificationService;

  @PostMapping("/signin")
  public ResponseEntity<ApiDataResponse<AccessTokenResponse>> authenticate(@RequestBody SignInDTO signInDTO) {

    return create(() -> authentificationService.login(signInDTO.getUserName(), signInDTO.getPassword()));
  }

  @PostMapping("/refresh")
  public ResponseEntity<ApiDataResponse<AccessTokenResponse>> refreshToken(
      @RequestBody RefreshTokenDTO refreshTokenDTO) {

    return create(() -> authentificationService.refreshToken(refreshTokenDTO));
  }

  @DeleteMapping("/logout")
  public ResponseEntity<Void> logout(@RequestBody RefreshTokenDTO refreshTokenDTO) {

    return noContent(() -> authentificationService.logout(refreshTokenDTO));
  }

}
