package com.cherrysoft.ahorrosapp.services.dailysaving;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.params.DailySavingParams;

public interface PartialUpdateDailySavingUseCase {

  DailySaving partialUpdateDailySaving(DailySavingParams params, DailySaving updatedDailySaving);

}
