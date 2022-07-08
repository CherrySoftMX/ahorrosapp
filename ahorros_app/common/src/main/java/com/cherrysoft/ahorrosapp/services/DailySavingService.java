package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.models.DailySaving;
import com.cherrysoft.ahorrosapp.services.dailysaving.CreateDailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.DeleteDailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.GetDailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.PartialUpdateDailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.queries.DailySavingQueryParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DailySavingService {
  private final GetDailySavingUseCase getDailySavingUseCase;
  private final CreateDailySavingUseCase createDailySavingUseCase;
  private final PartialUpdateDailySavingUseCase partialUpdateDailySavingUseCase;
  private final DeleteDailySavingUseCase deleteDailySavingUseCase;

  public Optional<DailySaving> getDailySaving(DailySavingQueryParams params) {
    return getDailySavingUseCase.getDailySaving(params);
  }

  public DailySaving getDailySavingOrThrowIfNotPresent(DailySavingQueryParams params) {
    return getDailySavingUseCase.getDailySavingOrThrowIfNotPresent(params);
  }

  public DailySaving createDailySaving(
      DailySavingQueryParams params, DailySaving dailySaving
  ) {
    return createDailySavingUseCase.createDailySaving(params, dailySaving);
  }

  public DailySaving partialUpdateDailySaving(
      DailySavingQueryParams params, DailySaving dailySaving
  ) {
    return partialUpdateDailySavingUseCase.partialUpdateTodaySaving(params, dailySaving);
  }

  public DailySaving deleteDailySaving(DailySavingQueryParams params) {
    return deleteDailySavingUseCase.deleteDailySaving(params);
  }

}
