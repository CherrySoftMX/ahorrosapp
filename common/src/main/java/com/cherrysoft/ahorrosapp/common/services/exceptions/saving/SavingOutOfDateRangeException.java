package com.cherrysoft.ahorrosapp.common.services.exceptions.saving;

import com.cherrysoft.ahorrosapp.common.core.models.SavingsDateRange;

import java.time.LocalDate;

public class SavingOutOfDateRangeException extends RuntimeException {

  public SavingOutOfDateRangeException(LocalDate savingDate, SavingsDateRange savingsDateRange) {
    super(String.format("The saving with date: %s is out of date range: %s", savingDate, savingsDateRange));
  }

}
