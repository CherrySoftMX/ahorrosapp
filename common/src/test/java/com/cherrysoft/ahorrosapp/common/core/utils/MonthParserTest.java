package com.cherrysoft.ahorrosapp.common.core.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MonthParserTest {
  private MonthParser monthParser;

  @BeforeEach
  void init() {
    this.monthParser = new MonthParser();
  }

  @Test
  void shouldReturnStartAndEndOfMonth_whenValidMMYYYYString() {
    String july2022 = "07-2022";
    monthParser.setMMYYYYString(july2022);

    LocalDate result = monthParser.startOfMonth();

    assertThat(result.getMonth(), is(Month.JULY));
    assertThat(result.getYear(), is(2022));
  }

  @Test
  void shouldThrowException_whenMonthIsNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      monthParser.startOfMonth();
    });
  }

}
