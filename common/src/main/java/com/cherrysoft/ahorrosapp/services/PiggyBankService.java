package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.models.User;
import com.cherrysoft.ahorrosapp.core.params.SavingParams;
import com.cherrysoft.ahorrosapp.core.params.piggybank.UpdatePiggyBankParams;
import com.cherrysoft.ahorrosapp.repositories.PiggyBankRepository;
import com.cherrysoft.ahorrosapp.services.exceptions.piggybank.PiggyBankNameNotAvailableException;
import com.cherrysoft.ahorrosapp.services.exceptions.piggybank.PiggyBankNotFoundException;
import com.cherrysoft.ahorrosapp.utils.BeanUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PiggyBankService {
  private final UserService userService;
  private final PiggyBankRepository pbRepository;

  public PiggyBank getPiggyBank(SavingParams params) {
    return getPiggyBankByName(params.getOwnerUsername(), params.getPbName());
  }

  public PiggyBank getPiggyBankByName(String pbName, String ownerUsername) {
    User owner = userService.getUserByUsername(ownerUsername);
    return getPiggyBankByName(pbName, owner);
  }

  public PiggyBank getPiggyBankByName(String pbName, User owner) {
    return pbRepository
        .findByNameAndOwner(pbName, owner)
        .orElseThrow(() -> new PiggyBankNotFoundException(pbName));
  }

  public PiggyBank addPiggyBankTo(PiggyBank pb, String ownerUsername) {
    User owner = userService.getUserByUsername(ownerUsername);
    ensureUniquePiggyBankNameForOwner(pb.getName(), owner);
    pb.setStartSavingsToTodayIfEmpty();
    pb.ensureSavingsIntervalIntegrity();
    owner.addPiggyBank(pb);
    userService.partialUpdateUser(ownerUsername, owner);
    return getPiggyBankByName(pb.getName(), owner);
  }

  public PiggyBank partialUpdatePiggyBank(UpdatePiggyBankParams params) {
    User owner = userService.getUserByUsername(params.getOwnerUsername());
    PiggyBank pb = getPiggyBankByName(owner, params.getOldPbName());
    if (!params.getOldPbName().equals(params.getUpdatedPb().getName())) {
      ensureUniquePiggyBankNameForOwner(owner, params.getUpdatedPb().getName());
    }
    BeanUtils.copyProperties(params.getUpdatedPb(), pb);
    pb.ensureSavingsIntervalIntegrity();
    return pbRepository.save(pb);
  }

  public PiggyBank deletePiggyBank(String pbName, String ownerUsername) {
    User owner = userService.getUserByUsername(ownerUsername);
    PiggyBank pb = getPiggyBankByName(pbName, owner);
    owner.removePiggyBank(pb);
    pbRepository.delete(pb);
    return pb;
  }

  private void ensureUniquePiggyBankNameForOwner(String pbName, User owner) {
    boolean pbNameAlreadyTaken = pbRepository.existsByNameAndOwner(pbName, owner);
    if (pbNameAlreadyTaken) {
      throw new PiggyBankNameNotAvailableException(pbName, owner.getUsername());
    }
  }

}
