package com.cherrysoft.ahorrosapp.core.splitters;

import com.cherrysoft.ahorrosapp.core.IntervalSavingsSummary;
import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@AllArgsConstructor
public class SavingsSplit {
  @Accessors(fluent = true)
  private String splitRepresentation;
  private List<DailySaving> dailySavings;

  public IntervalSavingsSummary summary() {
    return new IntervalSavingsSummary(dailySavings);
  }

  @Override
  public String toString() {
    return splitRepresentation;
  }

}
