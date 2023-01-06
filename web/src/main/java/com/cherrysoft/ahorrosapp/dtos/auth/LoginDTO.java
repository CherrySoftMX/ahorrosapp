package com.cherrysoft.ahorrosapp.dtos.auth;

import com.cherrysoft.ahorrosapp.dtos.validation.Password;
import com.cherrysoft.ahorrosapp.dtos.validation.Username;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {
  @Username
  @NotBlank(message = "A username is required.")
  private final String username;

  @Password
  @NotBlank(message = "A password is required.")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private final String password;

  @Override
  public String toString() {
    return "LoginDTO{" +
        "username='" + username + '\'' +
        ", password=******** }";
  }

}
