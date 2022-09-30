package com.cherrysoft.ahorrosapp.services.dailysaving;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.params.DailySavingParams;

import java.util.Optional;

public interface GetDailySavingUseCase {

  Optional<DailySaving> getDailySaving(DailySavingParams params);

  DailySaving getDailySavingOrThrowIfNotPresent(DailySavingParams params);

}
