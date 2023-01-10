package com.cherrysoft.ahorrosapp.web.security;

import com.cherrysoft.ahorrosapp.web.dtos.auth.TokenDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static java.util.stream.Collectors.joining;

@Component
public class TokenGenerator {
  private final JwtEncoder encoder;
  private final JwtEncoder refreshTokenEncoder;

  @Value("${token-expiration-time-hours}")
  private int tokenExpirationTimeHours;

  public TokenGenerator(
      JwtEncoder encoder,
      @Qualifier("jwtRefreshTokenEncoder") JwtEncoder refreshTokenEncoder
  ) {
    this.encoder = encoder;
    this.refreshTokenEncoder = refreshTokenEncoder;
  }

  public TokenDTO issueToken(Authentication authentication) {
    SecurityUser user = (SecurityUser) authentication.getPrincipal();
    String refreshToken = renovateRefreshTokenIfLessThan1WeekBeforeExpiration(authentication);
    return TokenDTO.builder()
        .username(user.getUsername())
        .accessToken(generateAccessToken(authentication))
        .refreshToken(refreshToken)
        .build();
  }

  private String renovateRefreshTokenIfLessThan1WeekBeforeExpiration(Authentication authentication) {
    if (authentication.getCredentials() instanceof Jwt) {
      Jwt jwt = (Jwt) authentication.getCredentials();
      if (lessThan1WeekBeforeExpiration(jwt)) {
        return generateRefreshToken(authentication);
      } else {
        return jwt.getTokenValue();
      }
    } else {
      return generateRefreshToken(authentication);
    }
  }

  private boolean lessThan1WeekBeforeExpiration(Jwt jwt) {
    Instant now = Instant.now();
    Instant expiresAt = jwt.getExpiresAt();
    Duration duration = Duration.between(now, expiresAt);
    long daysUntilExpired = duration.toDays();
    return daysUntilExpired < 7;
  }

  private String generateAccessToken(Authentication authentication) {
    Instant now = Instant.now();
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("manics")
        .issuedAt(now)
        .expiresAt(now.plus(tokenExpirationTimeHours, ChronoUnit.HOURS))
        .subject(authentication.getName())
        .claim("scope", extractScopes(authentication))
        .build();

    return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }

  private String generateRefreshToken(Authentication authentication) {
    Instant now = Instant.now();
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("manics")
        .issuedAt(now)
        .expiresAt(now.plus(30, ChronoUnit.DAYS))
        .subject(authentication.getName())
        .claim("scope", extractScopes(authentication))
        .build();

    return refreshTokenEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }

  private String extractScopes(Authentication authentication) {
    return authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(joining(" "));
  }

}
