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
  private String rawMonth;

  public MonthParser() {
    this(null);
  }

  public MonthParser(String rawMonth) {
    this(DateTimeFormatter.ofPattern(MONTH_YEAR_PATTERN), rawMonth);
  }

  public MonthParser(DateTimeFormatter formatter, String rawMonth) {
    this.rawMonth = rawMonth;
    this.formatter = formatter;
    parseMonth();
  }

  public LocalDate startOfMonth() {
    return parseResult.atDay(1);
  }

  public LocalDate endOfMonth() {
    return parseResult.atEndOfMonth();
  }

  public MonthParser setRawMonth(String rawMonth) {
    this.rawMonth = rawMonth;
    parseMonth();
    return this;
  }

  public void parseMonth() {
    if (!isNull(rawMonth)) {
      this.parseResult = YearMonth.parse(rawMonth, formatter);
    }
  }

}
