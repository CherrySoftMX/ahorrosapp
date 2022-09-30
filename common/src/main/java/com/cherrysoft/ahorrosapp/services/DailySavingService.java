package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.params.DailySavingParams;
import com.cherrysoft.ahorrosapp.services.dailysaving.CreateDailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.DeleteDailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.GetDailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.PartialUpdateDailySavingUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DailySavingService
    implements GetDailySavingUseCase, CreateDailySavingUseCase, PartialUpdateDailySavingUseCase, DeleteDailySavingUseCase {
  private final GetDailySavingUseCase getDailySavingUseCase;
  private final CreateDailySavingUseCase createDailySavingUseCase;
  private final PartialUpdateDailySavingUseCase partialUpdateDailySavingUseCase;
  private final DeleteDailySavingUseCase deleteDailySavingUseCase;

  @Override
  public Optional<DailySaving> getDailySaving(DailySavingParams params) {
    return getDailySavingUseCase.getDailySaving(params);
  }

  @Override
  public DailySaving getDailySavingOrThrowIfNotPresent(DailySavingParams params) {
    return getDailySavingUseCase.getDailySavingOrThrowIfNotPresent(params);
  }

  @Override
  public DailySaving createDailySaving(DailySavingParams params, DailySaving dailySaving) {
    return createDailySavingUseCase.createDailySaving(params, dailySaving);
  }

  @Override
  public DailySaving partialUpdateDailySaving(DailySavingParams params, DailySaving dailySaving) {
    return partialUpdateDailySavingUseCase.partialUpdateDailySaving(params, dailySaving);
  }

  @Override
  public DailySaving deleteDailySaving(DailySavingParams params) {
    return deleteDailySavingUseCase.deleteDailySaving(params);
  }

}
