package com.cherrysoft.ahorrosapp.services;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String username) {
    super("User with the username: " + username + " not found!");
  }
}
