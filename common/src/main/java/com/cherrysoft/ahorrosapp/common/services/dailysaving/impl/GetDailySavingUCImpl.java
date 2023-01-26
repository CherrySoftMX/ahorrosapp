package com.cherrysoft.ahorrosapp.common.services.dailysaving.impl;

import com.cherrysoft.ahorrosapp.common.core.interval.DatesInterval;
import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.common.core.models.specs.piggybank.GetPiggyBankSpec;
import com.cherrysoft.ahorrosapp.common.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.common.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.DailySavingUC;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.GetDailySavingUC;
import com.cherrysoft.ahorrosapp.common.services.exceptions.saving.SavingNotFoundException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Primary
@Component
public class GetDailySavingUCImpl extends DailySavingUC implements GetDailySavingUC {

  public GetDailySavingUCImpl(PiggyBankService pbService, DailySavingRepository dailySavingRepository) {
    super(pbService, dailySavingRepository);
  }

  @Override
  public Optional<DailySaving> getDailySaving(DailySavingSpec dailySavingSpec) {
    setDailySavingSpec(dailySavingSpec);
    PiggyBank pb = getPiggyBank();
    return dailySavingRepository.findByPiggyBankAndDate(pb, dailySavingSpec.getSavingDate());
  }

  @Override
  public List<DailySaving> getSavingsForInterval(GetPiggyBankSpec getPiggyBankSpec, DatesInterval intervalDate) {
    PiggyBank pb = pbService.getPiggyBankByName(getPiggyBankSpec.getOwnerUsername(), getPiggyBankSpec.getPiggyBankName());
    return dailySavingRepository.findByPiggyBankAndDateBetween(pb, intervalDate.startDay(), intervalDate.endDay());
  }

  @Override
  public DailySaving getDailySavingOrElseThrow(DailySavingSpec dailySavingSpec) {
    return getDailySaving(dailySavingSpec)
        .orElseThrow(() -> new SavingNotFoundException(dailySavingSpec.getSavingDate()));
  }

}
