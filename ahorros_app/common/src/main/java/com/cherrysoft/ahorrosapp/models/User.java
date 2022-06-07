package com.cherrysoft.ahorrosapp.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class User {
  @Id
  @GeneratedValue
  @Column(name = "user_id")
  private Long id;

  @Column(unique = true)
  private String username;

  @Column
  @NotNull
  private String password;

  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "owner",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private List<PiggyBank> piggyBanks;
}
