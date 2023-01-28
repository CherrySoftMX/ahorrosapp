package com.cherrysoft.ahorrosapp.common.services.exceptions.saving;

import com.cherrysoft.ahorrosapp.common.core.models.SavingsDateRange;

import java.time.LocalDate;

import static com.cherrysoft.ahorrosapp.common.utils.DateUtils.formatDate;

public class SavingOutOfDateRangeException extends RuntimeException {

  public SavingOutOfDateRangeException(LocalDate savingDate, SavingsDateRange savingsDateRange) {
    super(String.format("The saving with date: %s is out of date range: %s", formatDate(savingDate), savingsDateRange));
  }

}
