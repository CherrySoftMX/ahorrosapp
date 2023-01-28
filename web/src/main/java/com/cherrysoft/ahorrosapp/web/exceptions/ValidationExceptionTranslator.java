package com.cherrysoft.ahorrosapp.web.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.validation.ConstraintViolationAdviceTrait;
import org.zalando.problem.spring.web.advice.validation.MethodArgumentNotValidAdviceTrait;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.cherrysoft.ahorrosapp.web.utils.ToStringUtils.toJsonString;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNullElse;
import static java.util.stream.Collectors.toList;

@Slf4j
@RestControllerAdvice
public class ValidationExceptionTranslator implements ConstraintViolationAdviceTrait, MethodArgumentNotValidAdviceTrait {
  private static final String FIELD_ERRORS_KEY = "fieldErrors";

  @Override
  public ResponseEntity<Problem> handleConstraintViolation(ConstraintViolationException ex, NativeWebRequest request) {
    List<String> constraintViolations = ex.getConstraintViolations().stream()
        .map(ConstraintViolation::getMessage)
        .collect(toList());
    logConstraintViolations(constraintViolations, request);
    return ConstraintViolationAdviceTrait.super.handleConstraintViolation(ex, request);
  }

  private void logConstraintViolations(List<String> constraintsViolations, NativeWebRequest request) {
    HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
    String uri = nativeRequest.getRequestURI();
    String httpVerb = nativeRequest.getMethod();
    var entriesToLog = Map.of("uri", uri, "httpVerb", httpVerb, "constraintViolations", constraintsViolations);
    log.warn(toJsonString(entriesToLog));
  }

  @Override
  public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull NativeWebRequest request) {
    Map<String, String> validationErrors = extractValidationErrors(ex);
    Problem problem = Problem.builder()
        .withType(ErrorConstants.CONSTRAINT_VIOLATION_TYPE)
        .withTitle("Method argument not valid")
        .withStatus(defaultConstraintViolationStatus())
        .with(FIELD_ERRORS_KEY, validationErrors)
        .build();
    logMethodArgumentNotValidErrors(validationErrors, request);
    return create(ex, problem, request);
  }

  private Map<String, String> extractValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> result = new LinkedHashMap<>();
    ex.getBindingResult().getAllErrors().forEach(
        error -> result.put(((FieldError) error).getField(), error.getDefaultMessage())
    );
    return result;
  }

  private void logMethodArgumentNotValidErrors(Map<String, String> validationErrors, NativeWebRequest request) {
    HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
    String uri = nativeRequest.getRequestURI();
    String httpVerb = nativeRequest.getMethod();
    var entriesToLog = Map.of("uri", uri, "httpVerb", httpVerb, "validationErrors", validationErrors);
    log.warn(toJsonString(entriesToLog));
  }

  @ExceptionHandler
  public ResponseEntity<Problem> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, NativeWebRequest request) {
    logMethodArgumentTypeMismatchErrors(ex, request);
    return create(Status.BAD_REQUEST, ex, request);
  }

  private void logMethodArgumentTypeMismatchErrors(MethodArgumentTypeMismatchException ex, NativeWebRequest request) {
    HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
    String details = requireNonNullElse(ex.getMessage(), "null");
    String providedValue = nonNull(ex.getValue()) ? ex.getValue().toString() : "null";
    String uri = nativeRequest.getRequestURI();
    String httpVerb = nativeRequest.getMethod();
    var entriesToLog = Map.of("uri", uri, "httpVerb", httpVerb, "details", details, "providedValue", providedValue);
    log.warn(toJsonString(entriesToLog));
  }

}
