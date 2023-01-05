package com.cherrysoft.ahorrosapp.services.dailysaving;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.models.specs.DailySavingSpec;

import java.util.Optional;

public interface GetDailySavingUseCase {

  Optional<DailySaving> getDailySaving(DailySavingSpec dailySavingSpec);

  DailySaving getDailySavingOrElseThrow(DailySavingSpec dailySavingSpec);

}
