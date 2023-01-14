package com.cherrysoft.ahorrosapp.web.security.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.cherrysoft.ahorrosapp.web.utils.ToStringUtils.toJsonString;

@Slf4j
@RestControllerAdvice
public class AuthenticationAndAccessDenierLoggingHandler implements SecurityAdviceTrait {

  @Override
  public ResponseEntity<Problem> handleAuthentication(AuthenticationException e, NativeWebRequest request) {
    HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
    String hint = "Unauthorized access to a resource";
    int statusCode = HttpStatus.UNAUTHORIZED.value();
    String uri = nativeRequest.getRequestURI();
    var entriesToLog = Map.of("hint", hint, "statusCode", statusCode, "uri", uri);
    log.warn(toJsonString(entriesToLog));
    return SecurityAdviceTrait.super.handleAuthentication(e, request);
  }

  @Override
  public ResponseEntity<Problem> handleAccessDenied(AccessDeniedException e, NativeWebRequest request) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
    String hint = "Attempted access to a protected resource";
    int statusCode = HttpStatus.FORBIDDEN.value();
    String username = auth.getName();
    String uri = nativeRequest.getRequestURI();
    var entriesToLog = Map.of("hint", hint, "statusCode", statusCode, "username", username, "uri", uri);
    log.warn(toJsonString(entriesToLog));
    return SecurityAdviceTrait.super.handleAccessDenied(e, request);
  }

}
