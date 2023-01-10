package com.cherrysoft.ahorrosapp.common.core.fetchers.factories.imp;

import com.cherrysoft.ahorrosapp.common.core.fetchers.IntervalMonthFetcherStrategy;
import com.cherrysoft.ahorrosapp.common.core.fetchers.MonthlyFetcherStrategy;
import com.cherrysoft.ahorrosapp.common.core.fetchers.SavingsFetcherStrategy;
import com.cherrysoft.ahorrosapp.common.core.fetchers.factories.SavingsFetcherStrategyFactory;
import com.cherrysoft.ahorrosapp.common.core.models.specs.SavingsSummarySpec;
import com.cherrysoft.ahorrosapp.common.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.common.services.PiggyBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SavingsFetcherStrategyFactoryImpl implements SavingsFetcherStrategyFactory {
  private final PiggyBankService piggyBankService;
  private final DailySavingRepository dailySavingRepository;

  @Override
  public SavingsFetcherStrategy createMonthlyFetcherStrategy(SavingsSummarySpec spec) {
    return new MonthlyFetcherStrategy(spec, piggyBankService, dailySavingRepository);
  }

  @Override
  public SavingsFetcherStrategy createIntervalMonthFetcherStrategy(SavingsSummarySpec spec) {
    return new IntervalMonthFetcherStrategy(spec, piggyBankService, dailySavingRepository);
  }

}
