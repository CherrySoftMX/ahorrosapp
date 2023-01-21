package com.cherrysoft.ahorrosapp.common.core.models;

import com.cherrysoft.ahorrosapp.common.services.exceptions.piggybank.InvalidSavingsIntervalException;
import org.junit.jupiter.api.Test;

import static com.cherrysoft.ahorrosapp.common.utils.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class SavingsDateRangeTest {

  @Test
  void shouldReturnTrue_whenProvidedDateIsBetweenStartAndEndDates() {
    SavingsDateRange savingsDateRange = new SavingsDateRange(today(), aWeekInTheFuture());

    boolean shouldBeTrue = savingsDateRange.containedWithinSavingsRange(tomorrow());

    assertTrue(shouldBeTrue);
  }

  @Test
  void shouldReturnTrue_whenProvidedDateIsStartDate() {
    SavingsDateRange savingsDateRange = new SavingsDateRange(today(), aWeekInTheFuture());

    boolean shouldBeTrue = savingsDateRange.containedWithinSavingsRange(today());

    assertTrue(shouldBeTrue);
  }

  @Test
  void shouldReturnTrue_whenProvidedDateIsEndDate() {
    SavingsDateRange savingsDateRange = new SavingsDateRange(today(), aWeekInTheFuture());

    boolean shouldBeTrue = savingsDateRange.containedWithinSavingsRange(aWeekInTheFuture());

    assertTrue(shouldBeTrue);
  }

  @Test
  void shouldReturnFalse_whenProvidedDateIsBeforeStartDateOrAfterEndDate() {
    SavingsDateRange savingsDateRange = new SavingsDateRange(today(), aWeekInTheFuture());

    boolean shouldBeFalse = savingsDateRange.containedWithinSavingsRange(yesterday());

    assertFalse(shouldBeFalse);

    shouldBeFalse = savingsDateRange.containedWithinSavingsRange(aWeekInTheFuture().plusDays(1));

    assertFalse(shouldBeFalse);
  }

  @Test
  void shouldThrowException_whenEndDateIsBeforeStartDate() {
    SavingsDateRange savingsDateRange = new SavingsDateRange();
    savingsDateRange.setStartSavings(today());

    assertThrows(InvalidSavingsIntervalException.class, () -> {
      savingsDateRange.setEndSavings(yesterday());
      savingsDateRange.ensureSavingsDateRangeIntegrity();
    });
  }

  @Test
  void shouldThrowException_whenNoStartDateAndEndDateIsPast() {
    SavingsDateRange savingsDateRange = new SavingsDateRange();

    assertThrows(InvalidSavingsIntervalException.class, () -> {
      savingsDateRange.setEndSavings(yesterday());
      savingsDateRange.ensureSavingsDateRangeIntegrity();
    });
  }

}