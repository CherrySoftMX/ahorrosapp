package com.cherrysoft.ahorrosapp.core.fetchers.factories.imp;

import com.cherrysoft.ahorrosapp.core.fetchers.MonthlyFetcherStrategy;
import com.cherrysoft.ahorrosapp.core.fetchers.MonthlyIntervalFetcherStrategy;
import com.cherrysoft.ahorrosapp.core.fetchers.SavingsFetcherStrategy;
import com.cherrysoft.ahorrosapp.core.fetchers.factories.SavingsFetcherStrategyFactory;
import com.cherrysoft.ahorrosapp.core.queryparams.SavingsSummaryQueryParams;
import com.cherrysoft.ahorrosapp.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SavingsFetcherStrategyFactoryImp implements SavingsFetcherStrategyFactory {
  private final AutowireCapableBeanFactory beanFactory;

  @Override
  public SavingsFetcherStrategy createFetcherStrategy(SavingsSummaryQueryParams params) {
    String summaryType = params.getSummaryType();
    PiggyBankService pbService = beanFactory.getBean(PiggyBankService.class);
    DailySavingRepository dailySavingRepository = beanFactory.getBean(DailySavingRepository.class);
    if ("monthly".equals(summaryType)) {
      return new MonthlyFetcherStrategy(params, pbService, dailySavingRepository);
    } else if ("monthly-interval".equals(summaryType)) {
      return new MonthlyIntervalFetcherStrategy(params, pbService, dailySavingRepository);
    }
    throw new RuntimeException("No fetcher strategy found!");
  }

}
