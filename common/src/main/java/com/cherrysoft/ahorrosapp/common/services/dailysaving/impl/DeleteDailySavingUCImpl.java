package com.cherrysoft.ahorrosapp.common.services.dailysaving.impl;

import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.common.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.common.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.DailySavingUC;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.DeleteDailySavingUC;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.GetDailySavingUC;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class DeleteDailySavingUCImpl extends DailySavingUC implements DeleteDailySavingUC {
  private final GetDailySavingUC getDailySavingUC;

  public DeleteDailySavingUCImpl(
      PiggyBankService pbService,
      DailySavingRepository dailySavingRepository,
      GetDailySavingUC getDailySavingUC
  ) {
    super(pbService, dailySavingRepository);
    this.getDailySavingUC = getDailySavingUC;
  }

  @Override
  public DailySaving deleteDailySaving(DailySavingSpec dailySavingSpec) {
    setDailySavingSpec(dailySavingSpec);
    DailySaving dailySaving = getDailySavingUC.getDailySavingOrElseThrow(dailySavingSpec);
    PiggyBank pb = getPiggyBank();
    pb.removeDailySaving(dailySaving);
    dailySavingRepository.delete(dailySaving);
    return dailySaving;
  }

}
