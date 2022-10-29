package com.cherrysoft.ahorrosapp.core.collectors;

import com.cherrysoft.ahorrosapp.core.IntervalSavingsSummary;
import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SavingsGroup {
  private String groupName;
  private List<DailySaving> dailySavings;

  public IntervalSavingsSummary summary() {
    return new IntervalSavingsSummary(dailySavings);
  }

  @Override
  public String toString() {
    return groupName;
  }

}
