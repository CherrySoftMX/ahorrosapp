package com.cherrysoft.ahorrosapp.web.exceptions.handlers;

import com.cherrysoft.ahorrosapp.services.exceptions.piggybank.InvalidSavingsIntervalException;
import com.cherrysoft.ahorrosapp.services.exceptions.piggybank.PiggyBankNameNotAvailableException;
import com.cherrysoft.ahorrosapp.services.exceptions.piggybank.PiggyBankNotFoundException;
import com.cherrysoft.ahorrosapp.services.exceptions.saving.SavingNotFoundException;
import com.cherrysoft.ahorrosapp.services.exceptions.saving.SavingOutOfDateRangeException;
import com.cherrysoft.ahorrosapp.services.exceptions.user.UserNotFoundException;
import com.cherrysoft.ahorrosapp.services.exceptions.user.UsernameAlreadyTakenException;
import com.cherrysoft.ahorrosapp.web.exceptions.ApplicationException;
import com.cherrysoft.ahorrosapp.web.exceptions.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class ApplicationExceptionHandler {

  @ExceptionHandler(ApplicationException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Object> appExceptionHandler(final ApplicationException e) {
    return throwCustomException(e, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(UsernameAlreadyTakenException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> userExceptionHandler(final UsernameAlreadyTakenException e) {
    return throwCustomException(e, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<Object> userExceptionHandler(final UserNotFoundException e) {
    return throwCustomException(e, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(SavingNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<Object> userExceptionHandler(final SavingNotFoundException e) {
    return throwCustomException(e, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(SavingOutOfDateRangeException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> userExceptionHandler(final SavingOutOfDateRangeException e) {
    return throwCustomException(e, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(PiggyBankNameNotAvailableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> piggyBankExceptionHandler(final PiggyBankNameNotAvailableException e) {
    return throwCustomException(e, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(PiggyBankNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<Object> piggyBankExceptionHandler(final PiggyBankNotFoundException e) {
    return throwCustomException(e, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidSavingsIntervalException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> piggyBankExceptionHandler(final InvalidSavingsIntervalException e) {
    return throwCustomException(e, HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<Object> throwCustomException(final RuntimeException e, final HttpStatus status) {
    return new ResponseEntity<>(new ErrorResponse(e, status), status);
  }

}
