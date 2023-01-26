package com.cherrysoft.ahorrosapp.common.core.interval;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonthsIntervalTest {

  @Test
  void shouldReturnStartDayOfFirstMonthAndEndDayOfLastMonth() {
    String january2023 = "01-2023";
    String march2023 = "03-2023";
    MonthsInterval monthsInterval = new MonthsInterval(january2023, march2023);

    LocalDate startDay = monthsInterval.startDay();
    LocalDate endDay = monthsInterval.endDay();

    assertEquals(LocalDate.of(2023, Month.JANUARY, 1), startDay);
    assertEquals(LocalDate.of(2023, Month.MARCH, 31), endDay);
  }

}