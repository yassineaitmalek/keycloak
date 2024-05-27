package com.test.models.local;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

  private String id;

  private String firstName;

  private String lastName;

  private String email;

  private String username;

  private String password;

  private boolean isEmailVerified;

  private List<String> roles;

}
