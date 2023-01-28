package com.cherrysoft.ahorrosapp.common.core.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

import static com.cherrysoft.ahorrosapp.common.utils.DateUtils.formatDate;
import static com.cherrysoft.ahorrosapp.common.utils.DateUtils.today;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Entity
@Table(name = "daily_savings")
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DailySaving {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "daily_saving_id")
  private Long id;

  @Column
  private LocalDate date;

  @Column
  private BigDecimal amount;

  @Column
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "piggy_bank_id",
      referencedColumnName = "piggy_bank_id",
      foreignKey = @ForeignKey(name = "fk_piggy_bank_id")
  )
  @ToString.Exclude
  private PiggyBank piggyBank;

  public DailySaving(LocalDate date, BigDecimal amount) {
    this.date = date;
    this.amount = amount;
  }

  public YearMonth getYearMonthDate() {
    return YearMonth.from(getDate());
  }

  public DailySaving withDateOrToday(LocalDate date) {
    if (isNull(date)) {
      this.date = today();
    } else {
      this.date = date;
    }
    return this;
  }

  public void setDate(LocalDate date) {
    if (nonNull(date)) {
      this.date = date;
    }
  }

  public void setAmount(BigDecimal amount) {
    if (nonNull(amount)) {
      this.amount = amount;
    }
  }

  public void setDescription(String description) {
    if (nonNull(description)) {
      this.description = description;
    }
  }

  public LocalDate getDate() {
    if (isNull(date)) {
      date = today();
    }
    return date;
  }

  public String getDateString() {
    return formatDate(date);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    DailySaving that = (DailySaving) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

}
