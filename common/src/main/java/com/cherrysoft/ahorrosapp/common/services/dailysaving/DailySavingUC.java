package com.cherrysoft.ahorrosapp.common.services.dailysaving;

import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.common.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.common.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.common.services.exceptions.saving.SavingOutOfDateRangeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class DailySavingUC {
  protected final PiggyBankService pbService;
  protected final DailySavingRepository dailySavingRepository;
  protected DailySavingSpec dailySavingSpec;

  protected void ensureDailySavingDateIsWithinPbSavingsInterval(LocalDate date) {
    PiggyBank correspondingPb = getPiggyBank();
    if (!correspondingPb.containedWithinSavingsInterval(date)) {
      throw new SavingOutOfDateRangeException(date, correspondingPb.getSavingsDateRange());
    }
  }

  protected final PiggyBank getPiggyBank() {
    return pbService.getPiggyBankByName(dailySavingSpec.getOwnerUsername(), dailySavingSpec.getPbName());
  }

}
