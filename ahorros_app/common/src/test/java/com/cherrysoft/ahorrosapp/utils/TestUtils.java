package com.cherrysoft.ahorrosapp.utils;

import com.cherrysoft.ahorrosapp.models.User;

public class TestUtils {

  public static User newUser() {
    return User.builder()
        .username("hikingcarrot7")
        .password("password")
        .build();
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
