package com.cherrysoft.ahorrosapp.common.core.models;

import com.cherrysoft.ahorrosapp.common.services.exceptions.piggybank.InvalidSavingsIntervalException;
import com.cherrysoft.ahorrosapp.common.core.PiggyBankSummary;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import static com.cherrysoft.ahorrosapp.common.utils.DateUtils.today;
import static java.util.Objects.isNull;

@Entity
@Table(name = "piggy_banks")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Audited
public class PiggyBank {
  @Id
  @GeneratedValue
  @Column(name = "piggy_bank_id")
  @Setter(AccessLevel.NONE)
  private Long id;

  @Column
  private String name;

  @Column(nullable = false)
  @Builder.Default
  private BigDecimal initialAmount = BigDecimal.ZERO;

  @Column(nullable = false)
  @Builder.Default
  private BigDecimal borrowedAmount = BigDecimal.ZERO;

  @Column
  private LocalDate startSavings;

  @Column
  private LocalDate endSavings;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  private Calendar createdAt;

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
  @NotAudited
  @ToString.Exclude
  private List<DailySaving> dailySavings;

  public PiggyBankSummary getPiggyBankSummary() {
    return new PiggyBankSummary(this);
  }

  public SavingDateRange getSavingDateRange() {
    return new SavingDateRange(startSavings, endSavings);
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