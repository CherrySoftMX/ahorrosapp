package com.cherrysoft.ahorrosapp.utils;

import com.cherrysoft.ahorrosapp.models.PiggyBank;
import com.cherrysoft.ahorrosapp.models.User;

import java.util.ArrayList;
import java.util.List;

import static com.cherrysoft.ahorrosapp.utils.DateUtils.today;

public class TestUtils {

  public static class Users {

    public static User newUser() {
      return User.builder()
          .username("hiking")
          .password("password")
          .pbs(new ArrayList<>())
          .build();
    }

    public static List<User> newUsers() {
      return List.of(
          User.builder()
              .username("javier")
              .password("12345")
              .pbs(new ArrayList<>())
              .build(),
          User.builder()
              .username("diego")
              .password("password")
              .pbs(new ArrayList<>())
              .build(),
          User.builder()
              .username("nicolas")
              .password("password123")
              .pbs(new ArrayList<>())
              .build()
      );
    }

    public static User savedUser() {
      User newUser = newUser();
      return User.builder()
          .id(1L)
          .username(newUser.getUsername())
          .password(newUser.getPassword())
          .pbs(new ArrayList<>())
          .build();
    }
  }

  public static class PiggyBanks {

    public static PiggyBank newPiggyBank() {
      return PiggyBank.builder()
          .name("piggy")
          .startSavings(today())
          .endSavings(today().plusDays(7))
          .build();
    }

    public static PiggyBank newPiggyBankNoStartDate() {
      return PiggyBank.builder()
          .name("piggy")
          .endSavings(today().plusDays(5))
          .build();
    }

    public static PiggyBank newPiggyBankWithOwner() {
      User owner = Users.newUser();
      PiggyBank pb = newPiggyBank();
      pb.setOwner(owner);
      owner.addPiggyBank(pb);
      return pb;
    }

  }

}
