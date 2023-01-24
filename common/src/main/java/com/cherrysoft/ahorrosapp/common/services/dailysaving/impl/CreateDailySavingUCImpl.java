package com.cherrysoft.ahorrosapp.common.services.dailysaving.impl;

import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.common.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.common.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.CreateDailySavingUC;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.DailySavingUC;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.GetDailySavingUC;
import com.cherrysoft.ahorrosapp.common.utils.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Primary
@Component
public class CreateDailySavingUCImpl extends DailySavingUC implements CreateDailySavingUC {
  private final GetDailySavingUC getDailySavingUC;

  public CreateDailySavingUCImpl(
      PiggyBankService pbService,
      DailySavingRepository dailySavingRepository,
      GetDailySavingUC getDailySavingUC
  ) {
    super(pbService, dailySavingRepository);
    this.getDailySavingUC = getDailySavingUC;
  }

  @Override
  public DailySaving createDailySaving(DailySavingSpec dailySavingSpec) {
    setDailySavingSpec(dailySavingSpec);
    ensureDailySavingDateIsWithinPbSavingsInterval();
    return createDailySaving();
  }

  private DailySaving createDailySaving() {
    Optional<DailySaving> dailySavingMaybe = getDailySavingUC.getDailySaving(getDailySavingSpec());
    return dailySavingMaybe
        .map(this::overrideSavedDailySaving)
        .orElseGet(this::addDailySavingToPiggyBank);
  }

  private DailySaving overrideSavedDailySaving(DailySaving savedDailySaving) {
    DailySaving payload = getDailySavingSpec().getDailySaving();
    BeanUtils.copyProperties(payload, savedDailySaving);
    return dailySavingRepository.save(savedDailySaving);
  }

  private DailySaving addDailySavingToPiggyBank() {
    PiggyBank pb = getPiggyBank();
    DailySaving payload = getDailySavingSpec().getDailySaving();
    pb.addDailySaving(payload);
    return dailySavingRepository.save(payload);
  }

}
