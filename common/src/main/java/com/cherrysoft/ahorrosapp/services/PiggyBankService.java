package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.models.User;
import com.cherrysoft.ahorrosapp.core.models.specs.piggybank.UpdatePiggyBankSpec;
import com.cherrysoft.ahorrosapp.repositories.PiggyBankRepository;
import com.cherrysoft.ahorrosapp.services.exceptions.piggybank.PiggyBankNameNotAvailableException;
import com.cherrysoft.ahorrosapp.services.exceptions.piggybank.PiggyBankNotFoundException;
import com.cherrysoft.ahorrosapp.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PiggyBankService {
  private final UserService userService;
  private final PiggyBankRepository pbRepository;

  public Page<PiggyBank> getPiggyBanks(String ownerUsername, Pageable pageable) {
    return pbRepository.getPiggyBanksByOwner_Username(ownerUsername, pageable);
  }

  public PiggyBank getPiggyBankByName(String ownerUsername, String pbName) {
    User owner = userService.getUserByUsername(ownerUsername);
    return getPiggyBankByName(owner, pbName);
  }

  public PiggyBank getPiggyBankByName(User owner, String pbName) {
    return pbRepository
        .findByNameAndOwner(pbName, owner)
        .orElseThrow(() -> new PiggyBankNotFoundException(pbName));
  }

  public PiggyBank addPiggyBankTo(String ownerUsername, PiggyBank pb) {
    User owner = userService.getUserByUsername(ownerUsername);
    ensureUniquePiggyBankNameForOwner(owner, pb.getName());
    pb.setStartSavingsToTodayIfEmpty();
    pb.ensureSavingsIntervalIntegrity();
    owner.addPiggyBank(pb);
    return pbRepository.saveAndFlush(pb);
  }

  public PiggyBank partialUpdatePiggyBank(UpdatePiggyBankSpec params) {
    User owner = userService.getUserByUsername(params.getOwnerUsername());
    PiggyBank pb = getPiggyBankByName(owner, params.getOldPbName());
    if (!params.getOldPbName().equals(params.getUpdatedPb().getName())) {
      ensureUniquePiggyBankNameForOwner(owner, params.getUpdatedPb().getName());
    }
    BeanUtils.copyProperties(params.getUpdatedPb(), pb);
    pb.ensureSavingsIntervalIntegrity();
    return pbRepository.save(pb);
  }

  public PiggyBank deletePiggyBank(String ownerUsername, String pbName) {
    User owner = userService.getUserByUsername(ownerUsername);
    PiggyBank pb = getPiggyBankByName(owner, pbName);
    owner.removePiggyBank(pb);
    pbRepository.delete(pb);
    return pb;
  }

  private void ensureUniquePiggyBankNameForOwner(User owner, String pbName) {
    boolean pbNameAlreadyTaken = pbRepository.existsByNameAndOwner(pbName, owner);
    if (pbNameAlreadyTaken) {
      throw new PiggyBankNameNotAvailableException(pbName, owner.getUsername());
    }
  }

}
