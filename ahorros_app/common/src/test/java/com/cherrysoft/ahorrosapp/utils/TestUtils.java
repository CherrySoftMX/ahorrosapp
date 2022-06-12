package com.cherrysoft.ahorrosapp.utils;

import com.cherrysoft.ahorrosapp.models.User;

public class TestUtils {

  public static User getUser() {
    return User.builder()
        .username("hikingcarrot7")
        .password("password")
        .build();
  }

}
