package com.cherrysoft.ahorrosapp.services.exceptions.piggybank;

public class PiggyBankNameNotAvailableException extends RuntimeException {

  public PiggyBankNameNotAvailableException(String name, String ownerUsername) {
    super("The name: " + name + " is not available for the owner: " + ownerUsername);
  }

}
