package com.cherrysoft.ahorrosapp.security.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.cherrysoft.ahorrosapp.utils.ToStringUtils.toJsonString;

@Slf4j
public class BearerTokenAccessDeniedLoggingHandler implements AccessDeniedHandler {
  private final BearerTokenAccessDeniedHandler deniedHandler = new BearerTokenAccessDeniedHandler();

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException
  ) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String hint = "Attempted access to a protected resource";
    int statusCode = HttpStatus.FORBIDDEN.value();
    String username = auth.getName();
    String uri = request.getRequestURI();
    var entriesToLog = Map.of("hint", hint, "statusCode", statusCode, "username", username, "uri", uri);
    log.warn(toJsonString(entriesToLog));
    deniedHandler.handle(request, response, accessDeniedException);
  }

}
