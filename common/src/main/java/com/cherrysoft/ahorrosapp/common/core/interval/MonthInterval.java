package com.cherrysoft.ahorrosapp.common.core.interval;

import com.cherrysoft.ahorrosapp.common.core.utils.MonthParser;

import java.time.LocalDate;

public class MonthInterval implements DatesInterval {
  private final MonthParser monthParser;

  public MonthInterval(String month) {
    this.monthParser = new MonthParser(month);
  }

  @Override
  public LocalDate startDay() {
    return monthParser.startOfMonth();
  }

  @Override
  public LocalDate endDay() {
    return monthParser.endOfMonth();
  }

}
