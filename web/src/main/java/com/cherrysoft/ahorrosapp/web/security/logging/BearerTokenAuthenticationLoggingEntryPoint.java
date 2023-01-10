package com.cherrysoft.ahorrosapp.web.security.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.cherrysoft.ahorrosapp.web.utils.ToStringUtils.toJsonString;

@Slf4j
public class BearerTokenAuthenticationLoggingEntryPoint implements AuthenticationEntryPoint {
  private final BearerTokenAuthenticationEntryPoint entryPoint = new BearerTokenAuthenticationEntryPoint();

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
    String hint = "Unauthorized access to a resource";
    int statusCode = HttpStatus.UNAUTHORIZED.value();
    String uri = request.getRequestURI();
    var entriesToLog = Map.of("hint", hint, "statusCode", statusCode, "uri", uri);
    log.warn(toJsonString(entriesToLog));
    entryPoint.commence(request, response, authException);
  }

}
