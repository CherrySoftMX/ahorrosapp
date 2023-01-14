package com.cherrysoft.ahorrosapp.web.exceptions;

import com.cherrysoft.ahorrosapp.common.services.exceptions.piggybank.InvalidSavingsIntervalException;
import com.cherrysoft.ahorrosapp.common.services.exceptions.piggybank.PiggyBankNameNotAvailableException;
import com.cherrysoft.ahorrosapp.common.services.exceptions.piggybank.PiggyBankNotFoundException;
import com.cherrysoft.ahorrosapp.common.services.exceptions.saving.SavingNotFoundException;
import com.cherrysoft.ahorrosapp.common.services.exceptions.saving.SavingOutOfDateRangeException;
import com.cherrysoft.ahorrosapp.common.services.exceptions.user.UserNotFoundException;
import com.cherrysoft.ahorrosapp.common.services.exceptions.user.UsernameAlreadyTakenException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.*;
import org.zalando.problem.spring.web.advice.AdviceTrait;
import org.zalando.problem.spring.web.advice.validation.ConstraintViolationProblem;

import javax.servlet.http.HttpServletRequest;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionTranslator implements AdviceTrait {
  private static final String PATH_KEY = "path";
  private static final String VIOLATIONS_KEY = "violations";

  @Override
  public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, @NonNull NativeWebRequest request) {
    if (isNull(entity)) {
      return null;
    }
    Problem problem = entity.getBody();
    if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
      return entity;
    }

    HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
    String requestUri = nonNull(nativeRequest) ? nativeRequest.getRequestURI() : StringUtils.EMPTY;
    ProblemBuilder builder = Problem
        .builder()
        .withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE : problem.getType())
        .withTitle(problem.getTitle())
        .withStatus(problem.getStatus())
        .withDetail(problem.getDetail())
        .with(PATH_KEY, requestUri);
    if (problem instanceof ConstraintViolationProblem) {
      builder.with(VIOLATIONS_KEY, ((ConstraintViolationProblem) problem).getViolations());
    } else {
      builder.withCause(((DefaultProblem) problem).getCause()).withDetail(problem.getDetail()).withInstance(problem.getInstance());
      problem.getParameters().forEach(builder::with);
    }

    ThrowableProblem throwableProblem = builder.build();
    throwableProblem.setStackTrace(((ThrowableProblem) problem).getStackTrace());
    return new ResponseEntity<>(problem, entity.getHeaders(), entity.getStatusCode());
  }


  @ExceptionHandler
  public ResponseEntity<Problem> handleUsernameAlreadyTakenException(UsernameAlreadyTakenException ex, NativeWebRequest request) {
    return create(Status.BAD_REQUEST, ex, request, ErrorConstants.LOGIN_ALREADY_USED_TYPE);
  }

  @ExceptionHandler
  public ResponseEntity<Problem> handleUserNotFoundException(UserNotFoundException ex, NativeWebRequest request) {
    return create(Status.NOT_FOUND, ex, request);
  }

  @ExceptionHandler
  public ResponseEntity<Problem> handlePiggyBankNotFoundException(PiggyBankNotFoundException ex, NativeWebRequest request) {
    return create(Status.NOT_FOUND, ex, request);
  }

  @ExceptionHandler
  public ResponseEntity<Problem> handlePiggyBankNameNotAvailableException(PiggyBankNameNotAvailableException ex, NativeWebRequest request) {
    return create(Status.BAD_REQUEST, ex, request);
  }

  @ExceptionHandler
  public ResponseEntity<Problem> handleInvalidSavingsIntervalException(InvalidSavingsIntervalException ex, NativeWebRequest request) {
    return create(Status.BAD_REQUEST, ex, request);
  }

  @ExceptionHandler
  public ResponseEntity<Problem> handleSavingNotFoundException(SavingNotFoundException ex, NativeWebRequest request) {
    return create(Status.NOT_FOUND, ex, request);
  }

  @ExceptionHandler
  public ResponseEntity<Problem> handleSavingOutOfDateRangeException(SavingOutOfDateRangeException ex, NativeWebRequest request) {
    return create(Status.BAD_REQUEST, ex, request);
  }

}
