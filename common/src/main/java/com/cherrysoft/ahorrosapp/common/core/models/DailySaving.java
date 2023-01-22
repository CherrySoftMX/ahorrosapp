package com.cherrysoft.ahorrosapp.common.core.models;

import com.cherrysoft.ahorrosapp.common.utils.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Entity
@Table(name = "daily_savings")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DailySaving {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "daily_saving_id")
  private Long id;

  @Column
  private LocalDate date = DateUtils.today();

  @Column
  private BigDecimal amount;

  @Column
  private String description;

  @ManyToOne
  @JoinColumn(
      name = "piggy_bank_id",
      referencedColumnName = "piggy_bank_id",
      foreignKey = @ForeignKey(name = "fk_piggy_bank_id")
  )
  private PiggyBank piggyBank;

  public DailySaving(LocalDate date, BigDecimal amount) {
    this.date = date;
    this.amount = amount;
  }

  public YearMonth getYearMonthDate() {
    return YearMonth.from(getDate());
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

  public DailySaving setDate(LocalDate date) {
    if (nonNull(date)) {
      this.date = date;
    }
    return this;
  }

}
