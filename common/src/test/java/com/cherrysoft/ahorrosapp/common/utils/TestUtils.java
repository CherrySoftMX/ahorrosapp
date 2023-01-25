package com.cherrysoft.ahorrosapp.common.utils;

import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.core.models.SavingsDateRange;
import com.cherrysoft.ahorrosapp.common.core.utils.MonthParser;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class TestUtils {

  public static class PiggyBanks {

    public static PiggyBank newPiggyBankNoEndDate() {
      return PiggyBank.builder()
          .name("piggy")
          .savingsDateRange(new SavingsDateRange())
          .build();
    }

  }

  public static class Savings {

    public static List<DailySaving> generateSavingsForMonth(String month) {
      return generateSavingsForMonth(month, 0);
    }

    public static List<DailySaving> generateSavingsForMonth(String month, double amountPerDay) {
      MonthParser monthParser = new MonthParser(month);
      int totalMonthDays = monthParser.endOfMonth().getDayOfMonth();
      return IntStream.range(0, totalMonthDays)
          .mapToObj(i -> new DailySaving(
              monthParser.startOfMonth().plusDays(i),
              BigDecimal.valueOf(amountPerDay)
          ))
          .collect(toList());
    }

  }

}
