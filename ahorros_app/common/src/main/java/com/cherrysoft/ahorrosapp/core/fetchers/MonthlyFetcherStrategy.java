package com.cherrysoft.ahorrosapp.core.fetchers;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.queryparams.SavingsSummaryQueryParams;
import com.cherrysoft.ahorrosapp.core.utils.MonthParser;
import com.cherrysoft.ahorrosapp.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;

import java.util.List;

public class MonthlyFetcherStrategy implements SavingsFetcherStrategy {
  private final SavingsSummaryQueryParams params;
  private final MonthParser monthParser;
  private final PiggyBankService pbService;
  private final DailySavingRepository dailySavingRepository;

  public MonthlyFetcherStrategy(
      SavingsSummaryQueryParams params,
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
    monthParser.setRawDate(params.getRawDate());
    PiggyBank pb = pbService.getPiggyBankByName(params);
    return dailySavingRepository.findByPiggyBankAndDateBetween(pb, monthParser.startOfMonth(), monthParser.endOfMonth());
  }

}
