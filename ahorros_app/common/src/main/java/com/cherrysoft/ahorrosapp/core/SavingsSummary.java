package com.cherrysoft.ahorrosapp.core;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Getter
@AllArgsConstructor
public class SavingsSummary {
  private List<DailySaving> savings;

  public BigDecimal getAverage() {
    BigDecimal total = getTotal();
    return total.divide(BigDecimal.valueOf(savings.size()), RoundingMode.DOWN);
  }

  public BigDecimal getTotal() {
    BigDecimal total = BigDecimal.ZERO;
    for (DailySaving saving : savings) {
      total = total.add(saving.getAmount());
    }
    return total;
  }

}
