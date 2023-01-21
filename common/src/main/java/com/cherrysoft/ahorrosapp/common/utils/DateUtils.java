package com.cherrysoft.ahorrosapp.common.utils;

import java.time.LocalDate;

public class DateUtils {
  public static final String DAY_MONTH_YEAR_PATTERN = "dd-MM-yyyy";
  public static final String MONTH_YEAR_PATTERN = "MM-yyyy";

  public static LocalDate today() {
    return LocalDate.now();
  }

  public static LocalDate tomorrow() {
    return today().plusDays(1);
  }

  public static LocalDate aWeekInTheFuture() {
    return today().plusDays(7);
  }

  public static LocalDate yesterday() {
    return today().minusDays(1);
  }

}
