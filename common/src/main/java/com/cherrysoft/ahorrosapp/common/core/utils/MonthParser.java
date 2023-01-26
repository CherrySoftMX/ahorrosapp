package com.cherrysoft.ahorrosapp.common.core.utils;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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
    this(mmyyyyString, MM_YYYY_FORMATTER);
  }

  public MonthParser(String mmyyyyString, DateTimeFormatter formatter) {
    this.mmyyyyString = mmyyyyString;
    this.formatter = formatter;
    parseMonth();
  }

  public LocalDate startOfMonth() {
    ensureParseResultIsNotNull();
    return parseResult.atDay(1);
  }

  public LocalDate endOfMonth() {
    ensureParseResultIsNotNull();
    return parseResult.atEndOfMonth();
  }

  private void ensureParseResultIsNotNull() {
    if (isNull(parseResult)) {
      throw new IllegalArgumentException("Month cannot be null!");
    }
  }

  public MonthParser setMMYYYYString(String mmyyyyString) {
    this.mmyyyyString = mmyyyyString;
    parseMonth();
    return this;
  }

  private void parseMonth() {
    if (nonNull(mmyyyyString)) {
      this.parseResult = YearMonth.parse(mmyyyyString, formatter);
    }
  }

}
