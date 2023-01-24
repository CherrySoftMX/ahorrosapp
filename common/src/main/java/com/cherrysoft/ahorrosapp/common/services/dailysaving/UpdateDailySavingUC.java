package com.cherrysoft.ahorrosapp.common.services.dailysaving;

import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.specs.DailySavingSpec;

public interface UpdateDailySavingUC {

  DailySaving updateDailySaving(DailySavingSpec dailySavingSpec);

}
