package com.cherrysoft.ahorrosapp.common.services.dailysaving.impl;

import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.common.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.common.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.DailySavingUC;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.GetDailySavingUC;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.UpdateDailySavingUC;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class UpdateDailySavingUCImpl extends DailySavingUC implements UpdateDailySavingUC {
  private final GetDailySavingUC getDailySavingUC;

  public UpdateDailySavingUCImpl(
      PiggyBankService pbService,
      DailySavingRepository dailySavingRepository,
      GetDailySavingUC getDailySavingUC
  ) {
    super(pbService, dailySavingRepository);
    this.getDailySavingUC = getDailySavingUC;
  }

  @Override
  public DailySaving updateDailySaving(DailySavingSpec dailySavingSpec) {
    setDailySavingSpec(dailySavingSpec);
    ensureDailySavingDateIsWithinPbSavingsInterval();
    return partialUpdateDailySaving();
  }

  private DailySaving partialUpdateDailySaving() {
    DailySaving savedDailySaving = getDailySavingUC.getDailySavingOrElseThrow(getDailySavingSpec());
    DailySaving payload = getDailySavingSpec().getDailySaving();

    savedDailySaving.setDate(payload.getDate());
    savedDailySaving.setAmount(payload.getAmount());
    savedDailySaving.setDescription(payload.getDescription());

    return dailySavingRepository.save(savedDailySaving);
  }

}
