package com.cherrysoft.ahorrosapp.web.dtos.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDTO {
  private final String username;
  private final String accessToken;
  private final String refreshToken;
}
