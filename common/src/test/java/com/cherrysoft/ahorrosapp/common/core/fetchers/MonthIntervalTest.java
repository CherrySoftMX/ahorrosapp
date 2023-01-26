package com.cherrysoft.ahorrosapp.common.core.fetchers;

import com.cherrysoft.ahorrosapp.common.core.interval.MonthInterval;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonthIntervalTest {

  @Test
  void shouldReturnStartAndEndDaysOfMonth() {
    String january2023 = "01-2023";
    MonthInterval monthInterval = new MonthInterval(january2023);

    LocalDate startDay = monthInterval.startDay();
    LocalDate endDay = monthInterval.endDay();

    assertEquals(LocalDate.of(2023, Month.JANUARY, 1), startDay);
    assertEquals(LocalDate.of(2023, Month.JANUARY, 31), endDay);
  }

}