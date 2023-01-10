package com.cherrysoft.ahorrosapp.common.services.dailysaving;

import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.specs.DailySavingSpec;

import java.util.Optional;

public interface GetDailySavingUseCase {

  Optional<DailySaving> getDailySaving(DailySavingSpec dailySavingSpec);

  DailySaving getDailySavingOrElseThrow(DailySavingSpec dailySavingSpec);

}
