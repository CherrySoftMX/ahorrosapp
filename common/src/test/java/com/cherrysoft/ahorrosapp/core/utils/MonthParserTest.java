package com.cherrysoft.ahorrosapp.core.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class MonthParserTest {
  private MonthParser monthParser;

  @BeforeEach
  void init() {
    monthParser = new MonthParser();
  }

  @Test
  void givenValidMonthYearString_thenReturnStartAndEndOfMonth() {
    monthParser.setMonthYearString("07-2022");

    LocalDate result = monthParser.startOfMonth();

    assertThat(result.getMonth(), is(Month.JULY));
    assertThat(result.getYear(), is(2022));
  }

}
