package com.cherrysoft.ahorrosapp.core.utils;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import static java.util.Objects.isNull;

@Data
public class MonthParser {
  public static final String MONTH_YEAR_PATTERN = "MM-yyyy";
  private final DateTimeFormatter formatter;
  @Setter(AccessLevel.NONE)
  private YearMonth parseResult;
  @Setter(AccessLevel.NONE)
  private String monthYearString;

  public MonthParser() {
    this(null);
  }

  public MonthParser(String monthYearString) {
    this(DateTimeFormatter.ofPattern(MONTH_YEAR_PATTERN), monthYearString);
  }

  public MonthParser(DateTimeFormatter formatter, String monthYearString) {
    this.monthYearString = monthYearString;
    this.formatter = formatter;
    parseMonth();
  }

  public LocalDate startOfMonth() {
    return parseResult.atDay(1);
  }

  public LocalDate endOfMonth() {
    return parseResult.atEndOfMonth();
  }

  public MonthParser setMonthYearString(String rawMonth) {
    this.monthYearString = rawMonth;
    parseMonth();
    return this;
  }

  public void parseMonth() {
    if (!isNull(monthYearString)) {
      this.parseResult = YearMonth.parse(monthYearString, formatter);
    }
  }

}
