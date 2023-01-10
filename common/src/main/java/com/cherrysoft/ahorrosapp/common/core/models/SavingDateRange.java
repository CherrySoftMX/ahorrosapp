package com.cherrysoft.ahorrosapp.common.core.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SavingDateRange {
  private final LocalDate startSavings;
  private final LocalDate endSavings;

  @Override
  public String toString() {
    return String.format("(%s - %s)", startSavings, endSavings);
  }

}
