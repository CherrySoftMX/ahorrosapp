package com.cherrysoft.ahorrosapp.services.exceptions.saving;

import com.cherrysoft.ahorrosapp.core.models.SavingDateRange;

import java.time.LocalDate;

public class SavingOutOfDateRangeException extends RuntimeException {

  public SavingOutOfDateRangeException(LocalDate savingDate, SavingDateRange savingDateRange) {
    super(String.format("The saving with date: %s is out of date range: %s", savingDate, savingDateRange));
  }

}
