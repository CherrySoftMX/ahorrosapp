package com.cherrysoft.ahorrosapp.dtos;

import com.cherrysoft.ahorrosapp.dtos.validation.OnCreate;
import com.cherrysoft.ahorrosapp.dtos.validation.Username;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
public class UserDTO {
  @Null
  private Long id;

  @Size(
      min = 5,
      max = 20,
      message = "Username must be between 5 and 20 chars."
  )
  @Username
  @NotBlank(groups = {OnCreate.class}, message = "A username is required.")
  private String username;

  @Size(
      min = 6,
      max = 16,
      message = "Password must be between 6 and 16 chars."
  )
  @NotBlank(groups = {OnCreate.class}, message = "A password is required.")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
}
