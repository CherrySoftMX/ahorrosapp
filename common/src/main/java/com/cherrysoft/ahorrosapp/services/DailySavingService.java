package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.services.dailysaving.CreateDailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.DeleteDailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.GetDailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.PartialUpdateDailySavingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailySavingService
    implements GetDailySavingUseCase, CreateDailySavingUseCase, PartialUpdateDailySavingUseCase, DeleteDailySavingUseCase {
  private final GetDailySavingUseCase getDailySavingUseCase;
  private final CreateDailySavingUseCase createDailySavingUseCase;
  private final PartialUpdateDailySavingUseCase partialUpdateDailySavingUseCase;
  private final DeleteDailySavingUseCase deleteDailySavingUseCase;

  @Override
  public Optional<DailySaving> getDailySaving(DailySavingSpec dailySavingSpec) {
    return getDailySavingUseCase.getDailySaving(dailySavingSpec);
  }

  @Override
  public DailySaving getDailySavingOrElseThrow(DailySavingSpec dailySavingSpec) {
    return getDailySavingUseCase.getDailySavingOrElseThrow(dailySavingSpec);
  }

  @Override
  public DailySaving createDailySaving(DailySavingSpec dailySavingSpec) {
    return createDailySavingUseCase.createDailySaving(dailySavingSpec);
  }

  @Override
  public DailySaving updateDailySaving(DailySavingSpec dailySavingSpec) {
    return partialUpdateDailySavingUseCase.updateDailySaving(dailySavingSpec);
  }

  @Override
  public DailySaving deleteDailySaving(DailySavingSpec dailySavingSpec) {
    return deleteDailySavingUseCase.deleteDailySaving(dailySavingSpec);
  }

}
