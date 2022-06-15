package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.models.PiggyBank;
import com.cherrysoft.ahorrosapp.models.User;
import com.cherrysoft.ahorrosapp.repositories.PiggyBankRepository;
import com.cherrysoft.ahorrosapp.services.exceptions.piggybank.PiggyBankNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PiggyBankService {
  private final UserService userService;
  private final PiggyBankRepository pbRepository;

  public PiggyBank getPiggyBankByName(String name, User owner) {
    return pbRepository
        .findByNameAndOwner(name, owner)
        .orElseThrow(() -> new PiggyBankNotFoundException(name));
  }

}
