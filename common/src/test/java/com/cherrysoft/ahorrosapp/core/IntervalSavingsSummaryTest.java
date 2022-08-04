package com.cherrysoft.ahorrosapp.core;

import com.cherrysoft.ahorrosapp.utils.TestUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntervalSavingsSummaryTest {
  private IntervalSavingsSummary intervalSavingsSummary;

  @Test
  void whenNoSavings_thenTotalAndAverageAmountAreZero() {
    intervalSavingsSummary = new IntervalSavingsSummary(Collections.emptyList());

    assertEquals(BigDecimal.ZERO, intervalSavingsSummary.getSavingsTotalAmount());
    assertEquals(BigDecimal.ZERO, intervalSavingsSummary.getAverageAmount());
  }

  @Test
  void calculatesTotalAndAverageAmount() {
    var savingsForJuly2022 = TestUtils.Savings.generateSavingsForMonth("07-2022", 100);
    intervalSavingsSummary = new IntervalSavingsSummary(savingsForJuly2022);

    assertEquals(BigDecimal.valueOf(3100.0), intervalSavingsSummary.getSavingsTotalAmount());
    assertEquals(BigDecimal.valueOf(100.0), intervalSavingsSummary.getAverageAmount());
  }

}