package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.core.IntervalSavingsSummary;
import com.cherrysoft.ahorrosapp.core.PiggyBankSummary;
import com.cherrysoft.ahorrosapp.core.fetchers.factories.SavingsFetcherStrategyFactory;
import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.queryparams.SavingsSummaryQueryParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SavingsSummaryService {
  private final SavingsFetcherStrategyFactory fetcherStrategyFactory;
  private final PiggyBankService pbService;

  public IntervalSavingsSummary calcIntervalSavingsSummary(SavingsSummaryQueryParams params) {
    var fetcherStrategy = fetcherStrategyFactory.createFetcherStrategy(params);
    return new IntervalSavingsSummary(fetcherStrategy.fetchSavings());
  }

  public PiggyBankSummary calcPiggyBankSummary(SavingsSummaryQueryParams params) {
    PiggyBank pb = pbService.getPiggyBankByName(params);
    return pb.getPiggyBankSummary();
  }

}
