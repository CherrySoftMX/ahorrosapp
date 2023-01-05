package com.cherrysoft.ahorrosapp.core;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

import static com.cherrysoft.ahorrosapp.utils.BigDecimalUtils.divide;

@Getter
@AllArgsConstructor
public class IntervalSavingsSummary implements SavingsSummary {
  private List<DailySaving> savings;

  @Override
  public BigDecimal getTotalAmount() {
    BigDecimal totalAmount = BigDecimal.ZERO;
    for (DailySaving saving : savings) {
      totalAmount = totalAmount.add(saving.getAmount());
    }
    return totalAmount;
  }

  @Override
  public BigDecimal getAverageAmount() {
    BigDecimal totalAmount = getTotalAmount();
    if (totalAmount.equals(BigDecimal.ZERO)) {
      return BigDecimal.ZERO;
    }
    return divide(totalAmount, savings.size());
  }

}
