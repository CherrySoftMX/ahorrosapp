package com.cherrysoft.ahorrosapp.services.dailysaving;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.queryparams.DailySavingQueryParams;

import java.util.Optional;

public interface GetDailySavingUseCase {

  Optional<DailySaving> getDailySaving(DailySavingQueryParams params);

  DailySaving getDailySavingOrThrowIfNotPresent(DailySavingQueryParams params);

}
