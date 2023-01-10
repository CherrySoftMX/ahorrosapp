package com.cherrysoft.ahorrosapp.common.core;

import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.utils.BigDecimalUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

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
    return BigDecimalUtils.divide(totalAmount, savings.size());
  }

}
