package com.cherrysoft.ahorrosapp.models;

import lombok.*;

import javax.persistence.*;

import static com.cherrysoft.ahorrosapp.utils.DateUtils.today;

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

  @Embedded
  private SavingInterval savingInterval;

  public void setStartSavingsToTodayIfEmpty() {
    if (!hasStartSavingsDate()) {
      savingInterval.setStartDate(today());
    }
  }

  public boolean hasStartSavingsDate() {
    return savingInterval.getStartDate() != null;
  }
}
