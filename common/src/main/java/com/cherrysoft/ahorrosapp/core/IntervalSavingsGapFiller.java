package com.cherrysoft.ahorrosapp.core;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class IntervalSavingsGapFiller {
  private final List<DailySaving> savings;
  private final LocalDate startDay;
  private final LocalDate endDay;

  public List<DailySaving> fillGaps() {
    LocalDate currentDay = startDay;
    List<DailySaving> filledGaps = new ArrayList<>();
    while (!currentDay.isAfter(endDay)) {
      if (existsSavingFor(currentDay)) {
        filledGaps.add(getSavingFor(currentDay));
      } else {
        filledGaps.add(createEmptySaving(currentDay));
      }
      currentDay = currentDay.plusDays(1);
    }
    return filledGaps;
  }

  private boolean existsSavingFor(LocalDate day) {
    return savings.stream().anyMatch(saving -> saving.getDate().equals(day));
  }

  private DailySaving getSavingFor(LocalDate day) {
    return savings.stream()
        .filter(saving -> saving.getDate().equals(day))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("No saving for day: " + day));
  }

  private DailySaving createEmptySaving(LocalDate day) {
    return new DailySaving(day, BigDecimal.ZERO);
  }

}
