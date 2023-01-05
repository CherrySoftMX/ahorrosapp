package com.cherrysoft.ahorrosapp.services.dailysaving;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.models.specs.DailySavingSpec;

public interface PartialUpdateDailySavingUseCase {

  DailySaving updateDailySaving(DailySavingSpec dailySavingSpec);

}
