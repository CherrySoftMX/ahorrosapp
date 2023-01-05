package com.cherrysoft.ahorrosapp.services.exceptions.saving;

import java.time.LocalDate;

public class SavingNotFoundException extends RuntimeException {

  public SavingNotFoundException(LocalDate date) {
    super(String.format("No saving for date: %s", date));
  }

}
