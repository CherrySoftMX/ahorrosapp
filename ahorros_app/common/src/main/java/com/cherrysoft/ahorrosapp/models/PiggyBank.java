package com.cherrysoft.ahorrosapp.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table
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
}
