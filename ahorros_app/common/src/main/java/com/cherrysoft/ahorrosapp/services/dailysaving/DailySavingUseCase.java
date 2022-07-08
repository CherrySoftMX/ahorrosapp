package com.cherrysoft.ahorrosapp.services.dailysaving;

import com.cherrysoft.ahorrosapp.models.DailySaving;
import com.cherrysoft.ahorrosapp.models.PiggyBank;
import com.cherrysoft.ahorrosapp.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.services.queries.DailySavingQueryParams;
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
