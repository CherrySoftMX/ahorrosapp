package com.cherrysoft.ahorrosapp.core;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Data
public class MonthParser {
  public static final String MONTH_YEAR_PATTERN = "MM-yyyy";
  private final DateTimeFormatter formatter;
  @Setter(AccessLevel.NONE)
  private YearMonth parseResult;
  private String rawDate;

  public MonthParser() {
    this(null);
  }

  public MonthParser(String rawDate) {
    this(DateTimeFormatter.ofPattern(MONTH_YEAR_PATTERN), rawDate);
  }

  public MonthParser(DateTimeFormatter formatter, String rawDate) {
    this.rawDate = rawDate;
    this.formatter = formatter;
    parseRawDate();
  }

  public LocalDate startOfMonth() {
    return parseResult.atDay(1);
  }

  public LocalDate endOfMonth() {
    return parseResult.atEndOfMonth();
  }

  public void setRawDate(String rawDate) {
    this.rawDate = rawDate;
    parseRawDate();
  }

  public void parseRawDate() {
    if (!Objects.isNull(rawDate)) {
      this.parseResult = YearMonth.parse(rawDate, formatter);
    }
  }

}
