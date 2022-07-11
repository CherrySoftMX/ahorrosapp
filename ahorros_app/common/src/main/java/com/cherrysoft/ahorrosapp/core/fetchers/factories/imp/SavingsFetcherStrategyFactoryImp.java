package com.cherrysoft.ahorrosapp.core.fetchers.factories.imp;

import com.cherrysoft.ahorrosapp.core.fetchers.MonthlyFetcherStrategy;
import com.cherrysoft.ahorrosapp.core.fetchers.SavingsFetcherStrategy;
import com.cherrysoft.ahorrosapp.core.fetchers.factories.SavingsFetcherStrategyFactory;
import com.cherrysoft.ahorrosapp.core.queryparams.SavingsSummaryQueryParams;
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
    if (summaryType.equals("monthly")) {
      var monthlyFetcherStrategy = new MonthlyFetcherStrategy(params);
      beanFactory.autowireBean(monthlyFetcherStrategy);
      return monthlyFetcherStrategy;
    }
    throw new RuntimeException("No fetcher strategy found!");
  }

}
