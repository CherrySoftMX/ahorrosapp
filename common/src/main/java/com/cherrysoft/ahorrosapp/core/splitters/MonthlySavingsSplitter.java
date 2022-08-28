package com.cherrysoft.ahorrosapp.core.splitters;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.utils.MonthParser;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MonthlySavingsSplitter implements SavingsSplitter {
  private final List<DailySaving> dailySavings;
  private final Map<YearMonth, List<DailySaving>> splitSavings = new HashMap<>();

  @Override
  public List<SavingsSplit> split() {
    dailySavings.forEach(saving -> {
      YearMonth yearMonth = YearMonth.from(saving.getDate());
      if (!splitSavings.containsKey(yearMonth)) {
        splitSavings.put(yearMonth, new ArrayList<>());
      }
      splitSavings.get(yearMonth).add(saving);
    });
    return toSavingsSplit();
  }

  private List<SavingsSplit> toSavingsSplit() {
    return splitSavings.entrySet()
        .stream()
        .map(entry -> createSavingsSplit(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  private SavingsSplit createSavingsSplit(YearMonth yearMonth, List<DailySaving> savings) {
    String monthYearString = yearMonth.format(MonthParser.MONTH_YEAR_FORMATTER);
    return new SavingsSplit(monthYearString, savings);
  }

}
