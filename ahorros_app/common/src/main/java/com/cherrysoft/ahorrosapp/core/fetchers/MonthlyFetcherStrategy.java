package com.cherrysoft.ahorrosapp.core.fetchers;

import com.cherrysoft.ahorrosapp.core.MonthParser;
import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.queryparams.SavingsSummaryQueryParams;
import com.cherrysoft.ahorrosapp.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MonthlyFetcherStrategy implements SavingsFetcherStrategy {
  private final SavingsSummaryQueryParams params;
  private PiggyBankService pbService;
  private DailySavingRepository dailySavingRepository;
  private final MonthParser monthParser;

  public MonthlyFetcherStrategy(SavingsSummaryQueryParams params) {
    this.params = params;
    this.monthParser = new MonthParser();
  }

  @Override
  public List<DailySaving> fetchSavings() {
    monthParser.setRawDate(params.getRawDate());
    PiggyBank pb = pbService.getPiggyBankByName(params);
    return dailySavingRepository.findByPiggyBankAndDateBetween(pb, monthParser.startOfMonth(), monthParser.endOfMonth());
  }

  @Autowired
  public void setDailySavingRepository(DailySavingRepository dailySavingRepository) {
    this.dailySavingRepository = dailySavingRepository;
  }

  @Autowired
  public void setPbService(PiggyBankService pbService) {
    this.pbService = pbService;
  }

}
