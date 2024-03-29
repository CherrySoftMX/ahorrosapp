package com.cherrysoft.ahorrosapp.common.core.models;

import com.cherrysoft.ahorrosapp.common.core.PiggyBankSummary;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Entity
@Table(name = "piggy_banks")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Audited
public class PiggyBank {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "piggy_bank_id")
  @Setter(AccessLevel.NONE)
  private Long id;

  @Column
  private String name;

  @Column
  private BigDecimal initialAmount = BigDecimal.ZERO;

  @Column
  private BigDecimal borrowedAmount = BigDecimal.ZERO;

  @Embedded
  @Builder.Default
  private SavingsDateRange savingsDateRange = new SavingsDateRange();

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  private Calendar createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "owner_user_id",
      referencedColumnName = "user_id",
      foreignKey = @ForeignKey(name = "fk_user_id")
  )
  @ToString.Exclude
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

  public void addDailySavings(List<DailySaving> dailySavings) {
    dailySavings.forEach(this::addDailySaving);
  }

  public void addDailySaving(DailySaving dailySaving) {
    if (isNull(dailySavings)) {
      dailySavings = new ArrayList<>();
    }
    dailySaving.setPiggyBank(this);
    dailySavings.add(dailySaving);
  }

  public void removeDailySaving(DailySaving dailySaving) {
    if (isNull(dailySavings)) {
      dailySavings = new ArrayList<>();
    }
    dailySaving.setPiggyBank(null);
    dailySavings.remove(dailySaving);
  }

  public PiggyBankSummary getPiggyBankSummary() {
    return new PiggyBankSummary(this);
  }

  public void setStartSavingsToTodayIfNull() {
    savingsDateRange.setStartSavingsToTodayIfNull();
  }

  public void setName(String name) {
    if (nonNull(name)) {
      this.name = name;
    }
  }

  public void setInitialAmount(BigDecimal initialAmount) {
    if (nonNull(initialAmount)) {
      this.initialAmount = initialAmount;
    }
  }

  public void setBorrowedAmount(BigDecimal borrowedAmount) {
    if (nonNull(borrowedAmount)) {
      this.borrowedAmount = borrowedAmount;
    }
  }

  public void setStartSavings(LocalDate startSavings) {
    if (nonNull(startSavings)) {
      savingsDateRange.setStartSavings(startSavings);
    }
  }

  public void setEndSavings(LocalDate endSavings) {
    if (nonNull(endSavings)) {
      savingsDateRange.setEndSavings(endSavings);
    }
  }

  public boolean containedWithinSavingsInterval(LocalDate date) {
    return savingsDateRange.containedWithinSavingsRange(date);
  }

  public void ensureSavingsIntervalIntegrity() {
    savingsDateRange.ensureSavingsDateRangeIntegrity();
  }

  public LocalDate getEndSavings() {
    return savingsDateRange.getEndSavings();
  }

  public LocalDate getStartSavings() {
    return savingsDateRange.getStartSavings();
  }

}
