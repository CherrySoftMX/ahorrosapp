package com.cherrysoft.ahorrosapp.common.services.dailysaving;

import com.cherrysoft.ahorrosapp.common.core.interval.DatesInterval;
import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.common.core.models.specs.piggybank.GetPiggyBankSpec;

import java.util.List;
import java.util.Optional;

public interface GetDailySavingUC {

  Optional<DailySaving> getDailySaving(DailySavingSpec dailySavingSpec);

  List<DailySaving> getSavingsForInterval(GetPiggyBankSpec getPiggyBankSpec, DatesInterval intervalDate);

  DailySaving getDailySavingOrElseThrow(DailySavingSpec dailySavingSpec);

}
