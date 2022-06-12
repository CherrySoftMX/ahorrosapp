package com.cherrysoft.ahorrosapp.services.exceptions;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String username) {
    super("User with the username: " + username + " not found!");
  }
}
