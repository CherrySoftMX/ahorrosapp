package com.cherrysoft.ahorrosapp.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@NoArgsConstructor
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

  public User(String username, String password) {
    this(null, username, password);
  }

  @Builder
  public User(Long id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = password;
  }

  public void addPiggyBank(PiggyBank pb) {
    piggyBanks.add(pb);
    pb.setOwner(this);
  }

  public void removePiggyBank(PiggyBank pb) {
    piggyBanks.remove(pb);
    pb.setOwner(null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    User user = (User) o;
    return id != null && Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
