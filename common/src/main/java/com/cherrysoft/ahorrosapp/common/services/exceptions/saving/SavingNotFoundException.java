package com.cherrysoft.ahorrosapp.common.services.exceptions.saving;

import java.time.LocalDate;

import static com.cherrysoft.ahorrosapp.common.utils.DateUtils.formatDate;

public class SavingNotFoundException extends RuntimeException {

  public SavingNotFoundException(LocalDate date) {
    super(String.format("No saving for date: %s", formatDate(date)));
  }

}
