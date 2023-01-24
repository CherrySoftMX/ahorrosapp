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

  protected void ensureDailySavingDateIsWithinPbSavingsInterval() {
    PiggyBank correspondingPb = getCorrespondingPiggyBank();
    LocalDate savingDate = dailySavingSpec.getSavingDate();
    if (!correspondingPb.containedWithinSavingsInterval(savingDate)) {
      throw new SavingOutOfDateRangeException(savingDate, correspondingPb.getSavingsDateRange());
    }
  }

  protected final PiggyBank getCorrespondingPiggyBank() {
    return pbService.getPiggyBankByName(dailySavingSpec.getOwnerUsername(), dailySavingSpec.getPbName());
  }

}
