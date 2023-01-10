package com.cherrysoft.ahorrosapp.web.security;

import com.cherrysoft.ahorrosapp.core.models.User;
import com.cherrysoft.ahorrosapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtToSecurityUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {
  private final UserService userService;

  @Override
  public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
    String username = jwt.getSubject();
    User user = userService.getUserByUsername(username);
    SecurityUser securityUser = new SecurityUser(user);
    return new UsernamePasswordAuthenticationToken(securityUser, jwt, securityUser.getAuthorities());
  }

}
