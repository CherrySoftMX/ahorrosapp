package com.cherrysoft.ahorrosapp.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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

  @ManyToOne
  @JoinColumn(
      name = "owner_user_id",
      referencedColumnName = "user_id",
      foreignKey = @ForeignKey(name = "fk_user_id")
  )
  private User owner;

  @Column
  private LocalDate startSavings;

  @Column
  private LocalDate endSavings;

  public void ensureSavingsIntervalIntegrity() {
    ensureEndDateIsPresentOrFutureIfNotStartDate();
    ensureStartDateIsBeforeEndDate();
  }

  public void ensureEndDateIsPresentOrFutureIfNotStartDate() {
    if (isNull(startSavings) && endSavings.isBefore(today())) {
      throw new RuntimeException("End date must be present or future.");
    }
  }

  public void ensureStartDateIsBeforeEndDate() {
    if (hasEndSavingsDate() && startSavings.isAfter(endSavings)) {
      throw new RuntimeException("Start date must be before end date.");
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
