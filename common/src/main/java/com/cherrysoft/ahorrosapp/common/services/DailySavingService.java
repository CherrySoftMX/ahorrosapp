package com.cherrysoft.ahorrosapp.common.services;

import com.cherrysoft.ahorrosapp.common.core.interval.DatesInterval;
import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.common.core.models.specs.piggybank.GetPiggyBankSpec;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.CreateDailySavingUC;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.DeleteDailySavingUC;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.GetDailySavingUC;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.UpdateDailySavingUC;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailySavingService
    implements GetDailySavingUC, CreateDailySavingUC, UpdateDailySavingUC, DeleteDailySavingUC {
  private final GetDailySavingUC getDailySavingUC;
  private final CreateDailySavingUC createDailySavingUC;
  private final UpdateDailySavingUC updateDailySavingUC;
  private final DeleteDailySavingUC deleteDailySavingUC;

  @Override
  public Optional<DailySaving> getDailySaving(DailySavingSpec dailySavingSpec) {
    return getDailySavingUC.getDailySaving(dailySavingSpec);
  }

  @Override
  public List<DailySaving> getSavingsForInterval(GetPiggyBankSpec getPiggyBankSpec, DatesInterval intervalDate) {
    return getDailySavingUC.getSavingsForInterval(getPiggyBankSpec, intervalDate);
  }

  @Override
  public DailySaving getDailySavingOrElseThrow(DailySavingSpec dailySavingSpec) {
    return getDailySavingUC.getDailySavingOrElseThrow(dailySavingSpec);
  }

  @Override
  public DailySaving createDailySaving(DailySavingSpec dailySavingSpec) {
    return createDailySavingUC.createDailySaving(dailySavingSpec);
  }

  @Override
  public DailySaving updateDailySaving(DailySavingSpec dailySavingSpec) {
    return updateDailySavingUC.updateDailySaving(dailySavingSpec);
  }

  @Override
  public DailySaving deleteDailySaving(DailySavingSpec dailySavingSpec) {
    return deleteDailySavingUC.deleteDailySaving(dailySavingSpec);
  }

}
