package com.cherrysoft.ahorrosapp.core.fetchers.factories;

import com.cherrysoft.ahorrosapp.core.fetchers.SavingsFetcherStrategy;
import com.cherrysoft.ahorrosapp.core.models.specs.SavingsSummarySpec;

public interface SavingsFetcherStrategyFactory {

  SavingsFetcherStrategy createFetcherStrategy(SavingsSummarySpec params);

}
