package com.cherrysoft.ahorrosapp.services.dailysaving;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.params.DailySavingParams;

public interface CreateDailySavingUseCase {

  DailySaving createDailySaving(DailySavingParams params, DailySaving dailySaving);

}
