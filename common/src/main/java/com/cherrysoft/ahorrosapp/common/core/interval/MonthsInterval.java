package com.cherrysoft.ahorrosapp.common.core.interval;

import com.cherrysoft.ahorrosapp.common.core.utils.MonthParser;

import java.time.LocalDate;

public class MonthsInterval implements DatesInterval {
  private final String startMonth;
  private final String endMonth;
  private final MonthParser monthParser;

  public MonthsInterval(String startMonth, String endMonth) {
    this.startMonth = startMonth;
    this.endMonth = endMonth;
    this.monthParser = new MonthParser();
  }

  @Override
  public LocalDate startDay() {
    return monthParser.setMMYYYYString(startMonth).startOfMonth();
  }

  @Override
  public LocalDate endDay() {
    return monthParser.setMMYYYYString(endMonth).endOfMonth();
  }

}
