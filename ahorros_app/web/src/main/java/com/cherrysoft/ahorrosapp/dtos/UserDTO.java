package com.cherrysoft.ahorrosapp.dtos;

import com.cherrysoft.ahorrosapp.dtos.validation.Username;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UserDTO {
  @Size(
      min = 5,
      max = 20,
      message = "Username must be between 5 and 20 chars."
  )
  @Username
  private String username;

  @Size(
      min = 6,
      max = 16,
      message = "Password must be between 6 and 16 chars."
  )
  private String password;
}
