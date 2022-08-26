package com.cherrysoft.ahorrosapp.core;

public enum SavingSummaryType {
  WEEKLY,
  MONTHLY,
  INTERVAL_MONTH {
    @Override
    public String toString() {
      return "interval-month";
    }
  },
  YEARLY;

  @Override
  public String toString() {
    return super.toString().toLowerCase();
  }

}
