package com.cherrysoft.ahorrosapp.dtos.summaries;

import com.cherrysoft.ahorrosapp.dtos.DailySavingDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PiggyBankSummaryDTO extends IntervalSavingsSummaryDTO {
  private BigDecimal initialAmount;
  private BigDecimal inHandAmount;
  private BigDecimal borrowedAmount;

  @Override
  @JsonIgnore
  public List<DailySavingDTO> getSavings() {
    return super.getSavings();
  }

}
