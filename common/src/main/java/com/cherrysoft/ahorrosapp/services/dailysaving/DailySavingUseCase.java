package com.cherrysoft.ahorrosapp.services.dailysaving;

import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.services.exceptions.saving.SavingOutOfDateRangeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class DailySavingUseCase {
  protected final PiggyBankService pbService;
  protected final DailySavingRepository dailySavingRepository;
  protected DailySavingSpec dailySavingSpec;

  protected void ensureDailySavingDateIsWithinPbSavingsInterval() {
    PiggyBank correspondingPb = getCorrespondingPiggyBank();
    LocalDate savingDate = dailySavingSpec.getSavingDate();
    if (!correspondingPb.containedWithinSavingsInterval(savingDate)) {
      throw new RuntimeException(":(");
    }
  }

  protected final PiggyBank getCorrespondingPiggyBank() {
    return pbService.getPiggyBankByName(dailySavingSpec.getOwnerUsername(), dailySavingSpec.getPbName());
  }

}
