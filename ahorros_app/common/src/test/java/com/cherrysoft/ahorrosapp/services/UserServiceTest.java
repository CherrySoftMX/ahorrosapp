package com.cherrysoft.ahorrosapp.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserServiceTest {
  @Autowired
  private UserService userService;

  @Test
  public void userServiceIsNotNull() {
    assertNotNull(userService);
  }

  @Test
  public void returnsAllUsersInDatabase() {
    assertEquals(userService.allUsers(), List.of());
  }
}