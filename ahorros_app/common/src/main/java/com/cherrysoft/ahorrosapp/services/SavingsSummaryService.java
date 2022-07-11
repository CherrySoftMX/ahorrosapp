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
    var savingsSummary = new SavingsSummary(fetcherStrategy.fetchSavings());
    System.out.println(savingsSummary.getSavings());
    System.out.println(savingsSummary.calculateTotal());
    System.out.println(savingsSummary.calculateAverage());
    return null;
  }

}
