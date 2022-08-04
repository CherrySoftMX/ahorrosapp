package com.cherrysoft.ahorrosapp.services.exceptions.piggybank;

public class PiggyBankNotFoundException extends RuntimeException {

  public PiggyBankNotFoundException(String name) {
    super("Piggy bank with name: " + name + " does NOT exist!");
  }
}
