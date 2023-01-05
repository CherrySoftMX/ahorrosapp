package com.cherrysoft.ahorrosapp.core.fetchers;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.models.specs.SavingsSummarySpec;
import com.cherrysoft.ahorrosapp.core.utils.MonthParser;
import com.cherrysoft.ahorrosapp.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;

import java.time.LocalDate;
import java.util.List;

public class MonthlyFetcherStrategy implements SavingsFetcherStrategy {
  private final SavingsSummarySpec params;
  private final MonthParser monthParser;
  private final PiggyBankService pbService;
  private final DailySavingRepository dailySavingRepository;

  public MonthlyFetcherStrategy(
      SavingsSummarySpec params,
      PiggyBankService pbService,
      DailySavingRepository dailySavingRepository
  ) {
    this.params = params;
    this.pbService = pbService;
    this.dailySavingRepository = dailySavingRepository;
    this.monthParser = new MonthParser(params.getMonth());
  }

  @Override
  public List<DailySaving> fetchSavings() {
    PiggyBank pb = pbService.getPiggyBankByName(params.getOwnerUsername(), params.getPbName());
    return dailySavingRepository.findByPiggyBankAndDateBetween(pb, startDay(), endDay());
  }

  @Override
  public LocalDate startDay() {
    return monthParser.startOfMonth();
  }

  @Override
  public LocalDate endDay() {
    return monthParser.endOfMonth();
  }

}
