package com.cherrysoft.ahorrosapp.common.services.exceptions.user;

public class UsernameAlreadyTakenException extends RuntimeException {

  public UsernameAlreadyTakenException(String username) {
    super("Username: " + username + " is already taken!");
  }
}
