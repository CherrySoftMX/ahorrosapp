package com.cherrysoft.ahorrosapp.models;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table
public class PiggyBank {
  @Id
  @GeneratedValue
  @Column(name = "piggy_bank_id")
  private Long id;

  @Column
  @NotNull
  private String name;

  @ManyToOne
  @JoinColumn(
      name = "owner_user_id",
      referencedColumnName = "user_id",
      foreignKey = @ForeignKey(name = "fk_user_id")
  )
  private User owner;

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }
}
