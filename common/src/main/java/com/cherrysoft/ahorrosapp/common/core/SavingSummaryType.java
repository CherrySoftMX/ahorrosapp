package com.cherrysoft.ahorrosapp.common.core;

import java.util.Arrays;

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

  public static SavingSummaryType of(String type) {
    return Arrays.stream(SavingSummaryType.values())
        .filter(t -> t.toString().equalsIgnoreCase(type))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid type: " + type));
  }

  @Override
  public String toString() {
    return super.toString().toLowerCase();
  }

}
