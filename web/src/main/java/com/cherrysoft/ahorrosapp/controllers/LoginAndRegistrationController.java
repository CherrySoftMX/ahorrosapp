package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.core.models.User;
import com.cherrysoft.ahorrosapp.dtos.UserDTO;
import com.cherrysoft.ahorrosapp.dtos.auth.LoginDTO;
import com.cherrysoft.ahorrosapp.dtos.auth.TokenDTO;
import com.cherrysoft.ahorrosapp.mappers.UserMapper;
import com.cherrysoft.ahorrosapp.security.SecurityUser;
import com.cherrysoft.ahorrosapp.security.TokenGenerator;
import com.cherrysoft.ahorrosapp.services.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LoginAndRegistrationController {
  private final UserService userService;
  private final UserMapper userMapper;
  private final TokenGenerator tokenGenerator;
  private final DaoAuthenticationProvider daoAuthenticationProvider;
  private final JwtAuthenticationProvider refreshTokenAuthProvider;

  public LoginAndRegistrationController(
      UserService userService,
      UserMapper userMapper,
      TokenGenerator tokenGenerator,
      DaoAuthenticationProvider daoAuthenticationProvider,
      @Qualifier("jwtRefreshTokenAuthProvider") JwtAuthenticationProvider refreshTokenAuthProvider
  ) {
    this.userService = userService;
    this.userMapper = userMapper;
    this.tokenGenerator = tokenGenerator;
    this.daoAuthenticationProvider = daoAuthenticationProvider;
    this.refreshTokenAuthProvider = refreshTokenAuthProvider;
  }

  @PostMapping("/login")
  public TokenDTO login(@RequestBody @Valid LoginDTO payload) {
    var token = UsernamePasswordAuthenticationToken.unauthenticated(payload.getUsername(), payload.getPassword());
    Authentication authentication = daoAuthenticationProvider.authenticate(token);
    return tokenGenerator.issueToken(authentication);
  }

  @PostMapping("/register")
  public TokenDTO register(@RequestBody @Valid UserDTO payload) {
    User newUser = userMapper.toUser(payload);
    User result = userService.createUser(newUser);
    SecurityUser securityUser = new SecurityUser(result);
    Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(securityUser, payload.getPassword(), securityUser.getAuthorities());
    return tokenGenerator.issueToken(authentication);
  }

  @PostMapping("/refresh-token")
  public TokenDTO refreshToken(@RequestBody TokenDTO payload) {
    Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(payload.getRefreshToken()));
    Jwt jwt = (Jwt) authentication.getCredentials();
    String username = jwt.getSubject();
    userService.ensureUserExistByUsername(username);
    return tokenGenerator.issueToken(authentication);
  }

}
