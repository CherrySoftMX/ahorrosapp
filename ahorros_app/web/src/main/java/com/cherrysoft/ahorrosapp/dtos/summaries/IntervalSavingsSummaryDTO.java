package com.cherrysoft.ahorrosapp.dtos.summaries;

import com.cherrysoft.ahorrosapp.dtos.DailySavingDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntervalSavingsSummaryDTO {
  private List<DailySavingDTO> savings;
  private BigDecimal totalAmount;
  private BigDecimal averageAmount;
}
