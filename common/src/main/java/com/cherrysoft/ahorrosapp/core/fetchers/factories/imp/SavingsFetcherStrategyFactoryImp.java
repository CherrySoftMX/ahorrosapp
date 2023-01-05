package com.cherrysoft.ahorrosapp.core.fetchers.factories.imp;

import com.cherrysoft.ahorrosapp.core.SavingSummaryType;
import com.cherrysoft.ahorrosapp.core.fetchers.MonthlyFetcherStrategy;
import com.cherrysoft.ahorrosapp.core.fetchers.MultipleMonthFetcherStrategy;
import com.cherrysoft.ahorrosapp.core.fetchers.SavingsFetcherStrategy;
import com.cherrysoft.ahorrosapp.core.fetchers.factories.SavingsFetcherStrategyFactory;
import com.cherrysoft.ahorrosapp.core.models.specs.SavingsSummarySpec;
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
  public SavingsFetcherStrategy createFetcherStrategy(SavingsSummarySpec params) {
    SavingSummaryType summaryType = params.getSummaryType();
    PiggyBankService pbService = beanFactory.getBean(PiggyBankService.class);
    DailySavingRepository dailySavingRepository = beanFactory.getBean(DailySavingRepository.class);
    switch (summaryType) {
      case MONTHLY:
        return new MonthlyFetcherStrategy(params, pbService, dailySavingRepository);
      case INTERVAL_MONTH:
        return new MultipleMonthFetcherStrategy(params, pbService, dailySavingRepository);
      default:
        throw new RuntimeException("No fetcher strategy found!");
    }
  }

}
