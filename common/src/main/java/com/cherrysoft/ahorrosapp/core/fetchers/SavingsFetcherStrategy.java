package com.cherrysoft.ahorrosapp.core.fetchers;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;

import java.time.LocalDate;
import java.util.List;

public interface SavingsFetcherStrategy {

  List<DailySaving> fetchSavings();

  LocalDate startDay();

  LocalDate endDay();

}
