package com.cherrysoft.ahorrosapp.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.util.Objects.requireNonNullElse;

@Component
public class DateUtils {
  public static final String DAY_MONTH_YEAR_PATTERN = "dd-MM-yyyy";
  public static final String MONTH_YEAR_PATTERN = "MM-yyyy";
  private static Clock clock;

  public static LocalDate today() {
    return LocalDate.now(requireNonNullElse(clock, Clock.systemDefaultZone()));
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

  public static String formatDate(LocalDate date) {
    return date.format(DateTimeFormatter.ofPattern(DAY_MONTH_YEAR_PATTERN));
  }

  @Autowired
  public void setClock(Clock clock) {
    DateUtils.clock = clock;
  }

}
