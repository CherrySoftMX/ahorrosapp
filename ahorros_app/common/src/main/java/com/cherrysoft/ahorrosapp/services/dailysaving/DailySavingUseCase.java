package com.cherrysoft.ahorrosapp.services.dailysaving;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.queryparams.DailySavingQueryParams;
import com.cherrysoft.ahorrosapp.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class DailySavingUseCase {
  protected final PiggyBankService pbService;
  protected final DailySavingRepository dailySavingRepository;

  protected DailySavingQueryParams params;
  protected DailySaving dailySaving;

  protected void ensureDailySavingDateIsWithinPbSavingsInterval() {
    PiggyBank correspondingPb = getCorrespondingPiggyBank();
    if (!correspondingPb.containedWithinSavingsInterval(params.getDate())) {
      throw new RuntimeException(":(");
    }
  }

  protected PiggyBank getCorrespondingPiggyBank() {
    return pbService.getPiggyBankByName(params.getPbName(), params.getOwnerUsername());
  }

}
