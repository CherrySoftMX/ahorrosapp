package com.cherrysoft.ahorrosapp.exceptions.handlers;

import com.cherrysoft.ahorrosapp.exceptions.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.cherrysoft.ahorrosapp.utils.ToStringUtils.toJsonString;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  @NonNull
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request
  ) {
    Map<String, String> validationErrors = extractValidationErrors(ex);
    String uri = extractUri(request);
    String httpVerb = extractHttpVerb(request);
    ErrorResponse errorResponse = new ErrorResponse(status, validationErrors);
    var entriesToLog = Map.of("uri", uri, "httpVerb", httpVerb, "validationErrors", validationErrors);
    log.warn(toJsonString(entriesToLog));
    return new ResponseEntity<>(errorResponse, status);
  }

  private Map<String, String> extractValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> result = new LinkedHashMap<>();
    ex.getBindingResult().getAllErrors().forEach(
        e -> result.put(((FieldError) e).getField(), e.getDefaultMessage())
    );
    return result;
  }

  private String extractUri(WebRequest webRequest) {
    ServletWebRequest servletWebRequest = (ServletWebRequest) webRequest;
    return servletWebRequest.getRequest().getRequestURI();
  }

  private String extractHttpVerb(WebRequest webRequest) {
    ServletWebRequest servletWebRequest = (ServletWebRequest) webRequest;
    return servletWebRequest.getHttpMethod().name();
  }

}
