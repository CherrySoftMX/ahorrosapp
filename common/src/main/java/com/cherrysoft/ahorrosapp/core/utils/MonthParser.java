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
  public static final String MM_YYYY_PATTERN = "MM-yyyy";
  public static final DateTimeFormatter MM_YYYY_FORMATTER = DateTimeFormatter.ofPattern(MM_YYYY_PATTERN);
  private final DateTimeFormatter formatter;
  @Setter(AccessLevel.NONE)
  private YearMonth parseResult;
  @Setter(AccessLevel.NONE)
  private String mmyyyyString;

  public MonthParser() {
    this(null);
  }

  public MonthParser(String mmyyyyString) {
    this(MM_YYYY_FORMATTER, mmyyyyString);
  }

  public MonthParser(DateTimeFormatter formatter, String mmyyyyString) {
    this.mmyyyyString = mmyyyyString;
    this.formatter = formatter;
    parseMonth();
  }

  public LocalDate startOfMonth() {
    return parseResult.atDay(1);
  }

  public LocalDate endOfMonth() {
    return parseResult.atEndOfMonth();
  }

  public MonthParser setMMYYYYString(String mmyyyyString) {
    this.mmyyyyString = mmyyyyString;
    parseMonth();
    return this;
  }

  private void parseMonth() {
    if (!isNull(mmyyyyString)) {
      this.parseResult = YearMonth.parse(mmyyyyString, formatter);
    }
  }

}
