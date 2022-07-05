package com.cherrysoft.ahorrosapp.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "daily_savings")
@Getter
@Setter
@ToString
public class DailySaving {
  @Id
  @GeneratedValue
  @Column(name = "daily_saving_id")
  private Long id;

  @Column
  private LocalDate date;

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
