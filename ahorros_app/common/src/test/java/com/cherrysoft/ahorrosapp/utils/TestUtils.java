package com.cherrysoft.ahorrosapp.utils;

import com.cherrysoft.ahorrosapp.models.PiggyBank;
import com.cherrysoft.ahorrosapp.models.SavingInterval;
import com.cherrysoft.ahorrosapp.models.User;

import java.time.LocalDate;
import java.util.List;

public class TestUtils {

  public static class Users {

    public static User newUser() {
      return User.builder()
          .username("hiking")
          .password("password")
          .build();
    }

    public static List<User> newUsers() {
      return List.of(
          User.builder().username("javier").password("12345").build(),
          User.builder().username("diego").password("password").build(),
          User.builder().username("nicolas").password("password123").build()
      );
    }

    public static User savedUser() {
      User newUser = newUser();
      return User.builder()
          .id(1L)
          .username(newUser.getUsername())
          .password(newUser.getPassword())
          .build();
    }
  }

  public static class PiggyBanks {

    public static PiggyBank newPiggyBank() {
      return PiggyBank.builder()
          .name("piggy")
          .savingInterval(new SavingInterval(
              LocalDate.now(),
              LocalDate.now().plusDays(7))
          )
          .build();
    }
  }

}
