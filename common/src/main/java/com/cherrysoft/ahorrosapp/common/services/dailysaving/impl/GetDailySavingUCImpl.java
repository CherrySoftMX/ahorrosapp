package com.cherrysoft.ahorrosapp.common.services.dailysaving.impl;

import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.common.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.common.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.DailySavingUC;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.GetDailySavingUC;
import com.cherrysoft.ahorrosapp.common.services.exceptions.saving.SavingNotFoundException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Primary
@Component
public class GetDailySavingUCImpl extends DailySavingUC implements GetDailySavingUC {

  public GetDailySavingUCImpl(
      PiggyBankService pbService,
      DailySavingRepository dailySavingRepository
  ) {
    super(pbService, dailySavingRepository);
  }

  @Override
  public Optional<DailySaving> getDailySaving(DailySavingSpec dailySavingSpec) {
    setDailySavingSpec(dailySavingSpec);
    PiggyBank pb = getCorrespondingPiggyBank();
    return dailySavingRepository.findByPiggyBankAndDate(pb, dailySavingSpec.getSavingDate());
  }

  @Override
  public DailySaving getDailySavingOrElseThrow(DailySavingSpec dailySavingSpec) {
    return getDailySaving(dailySavingSpec)
        .orElseThrow(() -> new SavingNotFoundException(dailySavingSpec.getSavingDate()));
  }

}
