package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.models.PiggyBank;
import com.cherrysoft.ahorrosapp.models.User;
import com.cherrysoft.ahorrosapp.repositories.PiggyBankRepository;
import com.cherrysoft.ahorrosapp.services.exceptions.piggybank.PiggyBankNameNotAvailableException;
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

  public PiggyBank addPiggyBankTo(String ownerUsername, PiggyBank pb) {
    User owner = userService.getUserByUsername(ownerUsername);
    ensureUniquePiggyBankNameForOwner(pb.getName(), owner);
    pb.setStartSavingsToTodayIfEmpty();
    owner.addPiggyBank(pb);
    userService.partialUpdateUser(ownerUsername, owner);
    return getPiggyBankByName(pb.getName(), owner);
  }

  private void ensureUniquePiggyBankNameForOwner(String pbName, User owner) {
    boolean pbNameAlreadyTaken = pbRepository.existsByNameAndOwner(pbName, owner);
    if (pbNameAlreadyTaken) {
      throw new PiggyBankNameNotAvailableException(pbName, owner.getUsername());
    }
  }

}
