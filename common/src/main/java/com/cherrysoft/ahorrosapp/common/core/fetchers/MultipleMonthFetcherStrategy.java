package com.cherrysoft.ahorrosapp.common.core.fetchers;

import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.common.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.specs.SavingsSummarySpec;
import com.cherrysoft.ahorrosapp.common.core.utils.MonthParser;

import java.time.LocalDate;
import java.util.List;

public class MultipleMonthFetcherStrategy implements SavingsFetcherStrategy {
  private final SavingsSummarySpec params;
  private final MonthParser monthParser;
  private final PiggyBankService pbService;
  private final DailySavingRepository dailySavingRepository;

  public MultipleMonthFetcherStrategy(
      SavingsSummarySpec params,
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
    PiggyBank pb = pbService.getPiggyBankByName(params.getOwnerUsername(), params.getPbName());
    return dailySavingRepository.findByPiggyBankAndDateBetween(pb, startDay(), endDay());
  }

  @Override
  public LocalDate startDay() {
    return monthParser.setMMYYYYString(params.getStartMonth()).startOfMonth();
  }

  @Override
  public LocalDate endDay() {
    return monthParser.setMMYYYYString(params.getEndMonth()).endOfMonth();
  }

}
