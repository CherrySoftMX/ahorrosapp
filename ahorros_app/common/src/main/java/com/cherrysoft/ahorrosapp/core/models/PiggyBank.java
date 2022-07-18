package com.cherrysoft.ahorrosapp.core.models;

import com.cherrysoft.ahorrosapp.core.PiggyBankSummary;
import com.cherrysoft.ahorrosapp.services.exceptions.piggybank.InvalidSavingsIntervalException;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.cherrysoft.ahorrosapp.utils.DateUtils.today;
import static java.util.Objects.isNull;

@Entity
@Table(name = "piggy_banks")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PiggyBank {
  @Id
  @GeneratedValue
  @Column(name = "piggy_bank_id")
  @Setter(AccessLevel.NONE)
  private Long id;

  @Column
  private String name;

  @Column
  private BigDecimal initialAmount;

  @Column
  private BigDecimal borrowedAmount;

  @ManyToOne
  @JoinColumn(
      name = "owner_user_id",
      referencedColumnName = "user_id",
      foreignKey = @ForeignKey(name = "fk_user_id")
  )
  private User owner;

  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "piggyBank",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  @ToString.Exclude
  private List<DailySaving> dailySavings;

  @Column
  private LocalDate startSavings;

  @Column
  private LocalDate endSavings;

  public PiggyBankSummary getPiggyBankSummary() {
    return new PiggyBankSummary(this);
  }

  public void addDailySaving(DailySaving dailySaving) {
    dailySaving.setPiggyBank(this);
    dailySavings.add(dailySaving);
  }

  public boolean containedWithinSavingsInterval(LocalDate date) {
    if (date.isBefore(startSavings)) {
      return false;
    }
    boolean isAfterEndDate = hasEndSavingsDate() && date.isAfter(endSavings);
    return !isAfterEndDate;
  }

  public void ensureSavingsIntervalIntegrity() {
    ensureEndDateIsPresentOrFutureIfNotStartDate();
    ensureStartDateIsBeforeEndDate();
  }

  public void ensureEndDateIsPresentOrFutureIfNotStartDate() {
    if (isNull(startSavings) && endSavings.isBefore(today())) {
      throw new InvalidSavingsIntervalException("End date must be present or future.");
    }
  }

  public void ensureStartDateIsBeforeEndDate() {
    if (hasEndSavingsDate()) {
      boolean startDateIsEqualOrAfterEndDate = !startSavings.isBefore(endSavings);
      if (startDateIsEqualOrAfterEndDate) {
        throw new InvalidSavingsIntervalException("Start date must be before end date.");
      }
    }
  }

  public void setStartSavingsToTodayIfEmpty() {
    if (isNull(startSavings)) {
      ensureEndDateIsPresentOrFutureIfNotStartDate();
      setStartSavings(today());
    }
  }

  public boolean hasStartSavingsDate() {
    return !isNull(startSavings);
  }

  public boolean hasEndSavingsDate() {
    return !isNull(endSavings);
  }

}
