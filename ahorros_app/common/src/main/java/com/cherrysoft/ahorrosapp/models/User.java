package com.cherrysoft.ahorrosapp.models;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
  @Id
  @GeneratedValue
  @Column(name = "user_id")
  @Setter(AccessLevel.NONE)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "owner",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  @ToString.Exclude
  private List<PiggyBank> piggyBanks;

  public User(Long id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.piggyBanks = new ArrayList<>();
  }
}
