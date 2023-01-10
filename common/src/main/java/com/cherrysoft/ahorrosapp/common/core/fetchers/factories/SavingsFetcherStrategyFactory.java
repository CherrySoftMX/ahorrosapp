package com.cherrysoft.ahorrosapp.common.core.fetchers.factories;

import com.cherrysoft.ahorrosapp.common.core.fetchers.SavingsFetcherStrategy;
import com.cherrysoft.ahorrosapp.common.core.models.specs.SavingsSummarySpec;

public interface SavingsFetcherStrategyFactory {

  SavingsFetcherStrategy createFetcherStrategy(SavingsSummarySpec params);

}
