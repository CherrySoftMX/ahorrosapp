package com.cherrysoft.ahorrosapp.core.fetchers.factories;

import com.cherrysoft.ahorrosapp.core.fetchers.SavingsFetcherStrategy;
import com.cherrysoft.ahorrosapp.core.params.SavingsSummaryParams;

public interface SavingsFetcherStrategyFactory {

  SavingsFetcherStrategy createFetcherStrategy(SavingsSummaryParams params);

}
