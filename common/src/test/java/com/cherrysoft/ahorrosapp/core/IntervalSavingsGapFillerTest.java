package com.cherrysoft.ahorrosapp.core;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.utils.MonthParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntervalSavingsGapFillerTest {
  private MonthParser monthParser;
  private IntervalSavingsGapFiller gapFiller;

  @BeforeEach()
  void setUp() {
    monthParser = new MonthParser();
  }

  @Test
  void givenA31DaysMonth_whenNoSavings_thenReturns31EmptySavings() {
    monthParser.setMonthYearString("07-2022");
    gapFiller = new IntervalSavingsGapFiller(List.of(), monthParser.startOfMonth(), monthParser.endOfMonth());

    var result = gapFiller.fillGaps();

    assertEquals(31, countEmptySavings(result));
  }

  @Test
  void givenA31DaysMonth_when2NonEmptySavings_thenReturns29EmptySavings() {
    monthParser.setMonthYearString("07-2022");
    var savings = List.of(
        new DailySaving(monthParser.startOfMonth(), BigDecimal.valueOf(100)),
        new DailySaving(monthParser.startOfMonth().plusDays(1), BigDecimal.valueOf(50))
    );
    gapFiller = new IntervalSavingsGapFiller(savings, monthParser.startOfMonth(), monthParser.endOfMonth());

    var result = gapFiller.fillGaps();

    assertEquals(29, countEmptySavings(result));
  }

  @Test
  void givenMultipleMonths_whenNoSavings_thenReturnsSumOfDaysOfEachMonthAsEmptySavings() {
    LocalDate startDay = monthParser.setMonthYearString("07-2022").startOfMonth();
    LocalDate endDay = monthParser.setMonthYearString("09-2022").endOfMonth();
    gapFiller = new IntervalSavingsGapFiller(List.of(), startDay, endDay);

    var result = gapFiller.fillGaps();

    assertEquals(31 + 31 + 30, countEmptySavings(result));
  }

  private long countEmptySavings(List<DailySaving> savings) {
    return savings.stream()
        .filter(saving -> saving.getAmount().equals(BigDecimal.ZERO))
        .count();
  }

}