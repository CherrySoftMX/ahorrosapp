package com.cherrysoft.ahorrosapp.services.exceptions.user;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String username) {
    super("User with the username: " + username + " not found!");
  }
}
