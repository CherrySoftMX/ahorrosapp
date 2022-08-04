package com.cherrysoft.ahorrosapp.core.fetchers.factories;

import com.cherrysoft.ahorrosapp.core.fetchers.SavingsFetcherStrategy;
import com.cherrysoft.ahorrosapp.core.queryparams.SavingsSummaryQueryParams;

public interface SavingsFetcherStrategyFactory {

  SavingsFetcherStrategy createFetcherStrategy(SavingsSummaryQueryParams params);

}
