package com.cherrysoft.ahorrosapp.dtos.auth;

import com.cherrysoft.ahorrosapp.dtos.validation.Username;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class LoginDTO {
  @Username
  @NotBlank(message = "A username is required.")
  private final String username;

  @Size(
      min = 6,
      max = 16,
      message = "Password must be between {min} and {max} chars."
  )
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
