package com.cherrysoft.ahorrosapp.services.dailysaving;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.queryparams.DailySavingQueryParams;

public interface DeleteDailySavingUseCase {

  DailySaving deleteDailySaving(DailySavingQueryParams params);

}
