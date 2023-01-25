package com.cherrysoft.ahorrosapp.common.core.models;

import com.cherrysoft.ahorrosapp.common.core.utils.MonthParser;
import com.cherrysoft.ahorrosapp.common.services.exceptions.piggybank.InvalidSavingsIntervalException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.util.Objects;

import static com.cherrysoft.ahorrosapp.common.utils.DateUtils.today;
import static java.util.Objects.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class SavingsDateRange {
  @Column
  private LocalDate startSavings;

  @Column
  private LocalDate endSavings;

  public static SavingsDateRange forMonth(String month) {
    MonthParser monthParser = new MonthParser(month);
    return new SavingsDateRange(monthParser.startOfMonth(), monthParser.endOfMonth());
  }

  public boolean containedWithinSavingsRange(LocalDate date) {
    if (date.isBefore(startSavings)) {
      return false;
    }
    boolean isAfterEndDate = nonNull(endSavings) && date.isAfter(endSavings);
    return !isAfterEndDate;
  }

  public void setStartSavingsToTodayIfNull() {
    if (isNull(startSavings)) {
      setStartSavings(today());
    }
  }

  public void setStartSavings(LocalDate startSavings) {
    this.startSavings = requireNonNullElse(startSavings, today());
  }

  public void setEndSavings(LocalDate endSavings) {
    this.endSavings = endSavings;
  }

  public void ensureSavingsDateRangeIntegrity() {
    ensureEndDateIsPresentOrFutureIfNoStartDate();
    ensureStartDateIsBeforeEndDate();
  }

  private void ensureEndDateIsPresentOrFutureIfNoStartDate() {
    if (isNull(endSavings)) {
      return;
    }
    if (isNull(startSavings) && endSavings.isBefore(today())) {
      throw new InvalidSavingsIntervalException("End date must be present or future.");
    }
  }

  private void ensureStartDateIsBeforeEndDate() {
    if (nonNull(endSavings)) {
      boolean startDateIsEqualOrAfterEndDate = !startSavings.isBefore(endSavings);
      if (startDateIsEqualOrAfterEndDate) {
        throw new InvalidSavingsIntervalException("Start date must be before the end date.");
      }
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
      return false;
    SavingsDateRange that = (SavingsDateRange) o;
    return startSavings != null && Objects.equals(startSavings, that.startSavings)
        && endSavings != null && Objects.equals(endSavings, that.endSavings);
  }

  @Override
  public int hashCode() {
    return hash(startSavings, endSavings);
  }

  @Override
  public String toString() {
    return String.format("(%s - %s)", startSavings, endSavings);
  }

}
