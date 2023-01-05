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
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private final Long id;

  @Username
  @NotBlank(groups = {OnCreate.class}, message = "A username is required.")
  private final String username;

  @Size(
      min = 6,
      max = 16,
      message = "Password must be between {min} and {max} chars."
  )
  @NotBlank(groups = {OnCreate.class}, message = "A password is required.")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private final String password;
}
