package com.cherrysoft.ahorrosapp.core.fetchers;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;

import java.util.List;

public interface SavingsFetcherStrategy {

  List<DailySaving> fetchSavings();

}
