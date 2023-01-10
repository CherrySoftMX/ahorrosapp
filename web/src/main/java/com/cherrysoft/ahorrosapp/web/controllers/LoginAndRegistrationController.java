package com.cherrysoft.ahorrosapp.web.controllers;

import com.cherrysoft.ahorrosapp.core.models.User;
import com.cherrysoft.ahorrosapp.services.UserService;
import com.cherrysoft.ahorrosapp.web.dtos.UserDTO;
import com.cherrysoft.ahorrosapp.web.dtos.auth.LoginDTO;
import com.cherrysoft.ahorrosapp.web.dtos.auth.TokenDTO;
import com.cherrysoft.ahorrosapp.web.mappers.UserMapper;
import com.cherrysoft.ahorrosapp.web.security.SecurityUser;
import com.cherrysoft.ahorrosapp.web.security.TokenGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import static com.cherrysoft.ahorrosapp.web.utils.ApiDocsConstants.*;

@RestController
@Tag(name = "Login and registration", description = "Login and registration for users")
@ApiResponses({
    @ApiResponse(ref = BAD_REQUEST_RESPONSE_REF, responseCode = "400"),
    @ApiResponse(ref = NOT_FOUND_RESPONSE_REF, responseCode = "404"),
    @ApiResponse(ref = INTERNAL_SERVER_ERROR_RESPONSE_REF, responseCode = "500")
})
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

  @Operation(summary = "Login for registered user")
  @ApiResponse(responseCode = "200", description = "User Logged In", content = {
      @Content(schema = @Schema(implementation = TokenDTO.class))
  })
  @PostMapping("/login")
  public TokenDTO login(@RequestBody @Valid LoginDTO payload) {
    var token = UsernamePasswordAuthenticationToken.unauthenticated(payload.getUsername(), payload.getPassword());
    Authentication authentication = daoAuthenticationProvider.authenticate(token);
    return tokenGenerator.issueToken(authentication);
  }

  @Operation(summary = "Registration of new user")
  @ApiResponse(responseCode = "200", description = "User Registered", content = {
      @Content(schema = @Schema(implementation = TokenDTO.class))
  })
  @PostMapping("/register")
  public TokenDTO register(@RequestBody @Valid UserDTO payload) {
    User newUser = userMapper.toUser(payload);
    User result = userService.createUser(newUser);
    SecurityUser securityUser = new SecurityUser(result);
    Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(securityUser, payload.getPassword(), securityUser.getAuthorities());
    return tokenGenerator.issueToken(authentication);
  }

  @Operation(summary = "Issues a new access token with the provided refresh token")
  @ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = TokenDTO.class))
  })
  @PostMapping("/refresh-token")
  public TokenDTO refreshToken(@RequestBody TokenDTO payload) {
    Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(payload.getRefreshToken()));
    Jwt jwt = (Jwt) authentication.getCredentials();
    String username = jwt.getSubject();
    userService.ensureUserExistByUsername(username);
    return tokenGenerator.issueToken(authentication);
  }

}
