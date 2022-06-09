package com.cherrysoft.ahorrosapp.models;

import lombok.*;

import javax.persistence.*;
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

  @Column(unique = true)
  private String username;

  @Column
  private String password;

  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "owner",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  @ToString.Exclude
  private List<PiggyBank> piggyBanks;
}
