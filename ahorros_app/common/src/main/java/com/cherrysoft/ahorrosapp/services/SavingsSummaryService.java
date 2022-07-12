package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.core.SavingsSummary;
import com.cherrysoft.ahorrosapp.core.fetchers.factories.SavingsFetcherStrategyFactory;
import com.cherrysoft.ahorrosapp.core.queryparams.SavingsSummaryQueryParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SavingsSummaryService {
  private final SavingsFetcherStrategyFactory fetcherStrategyFactory;

  public SavingsSummary getSavingsSummary(SavingsSummaryQueryParams params) {
    var fetcherStrategy = fetcherStrategyFactory.createFetcherStrategy(params);
    return new SavingsSummary(fetcherStrategy.fetchSavings());
  }

}
