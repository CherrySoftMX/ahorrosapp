package com.cherrysoft.ahorrosapp.services.dailysaving;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.queryparams.DailySavingQueryParams;

public interface PartialUpdateDailySavingUseCase {

  DailySaving partialUpdateDailySaving(DailySavingQueryParams params, DailySaving updatedDailySaving);

}
