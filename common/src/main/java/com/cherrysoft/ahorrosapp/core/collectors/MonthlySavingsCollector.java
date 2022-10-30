package com.cherrysoft.ahorrosapp.core.collectors;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.utils.MonthParser;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;
import java.util.List;
import java.util.TreeMap;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class MonthlySavingsCollector implements SavingsCollector {
  private final List<DailySaving> dailySavings;

  @Override
  public List<SavingsGroup> collect() {
    return dailySavings.stream()
        .collect(groupingBy(DailySaving::getYearMonthDate, TreeMap::new, toList())) // to order asc by month
        .entrySet().stream()
        .map(entry -> createSavingsGroup(entry.getKey(), entry.getValue()))
        .collect(toList());
  }

  private SavingsGroup createSavingsGroup(YearMonth yearMonth, List<DailySaving> groupedSavings) {
    String mmyyyyString = yearMonth.format(MonthParser.MM_YYYY_FORMATTER);
    return new SavingsGroup(mmyyyyString, groupedSavings);
  }

}
