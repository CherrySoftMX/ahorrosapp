package com.cherrysoft.ahorrosapp.core.fetchers;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.params.SavingsSummaryParams;
import com.cherrysoft.ahorrosapp.core.utils.MonthParser;
import com.cherrysoft.ahorrosapp.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;

import java.time.LocalDate;
import java.util.List;

public class MultipleMonthFetcherStrategy implements SavingsFetcherStrategy {
  private final SavingsSummaryParams params;
  private final MonthParser monthParser;
  private final PiggyBankService pbService;
  private final DailySavingRepository dailySavingRepository;

  public MultipleMonthFetcherStrategy(
      SavingsSummaryParams params,
      PiggyBankService pbService,
      DailySavingRepository dailySavingRepository
  ) {
    this.params = params;
    this.pbService = pbService;
    this.dailySavingRepository = dailySavingRepository;
    this.monthParser = new MonthParser();
  }

  @Override
  public List<DailySaving> fetchSavings() {
    PiggyBank pb = pbService.getPiggyBank(params);
    return dailySavingRepository.findByPiggyBankAndDateBetween(pb, startDay(), endDay());
  }

  @Override
  public LocalDate startDay() {
    return monthParser.setMonthYearString(params.getStartMonth()).startOfMonth();
  }

  @Override
  public LocalDate endDay() {
    return monthParser.setMonthYearString(params.getEndMonth()).endOfMonth();
  }

}
