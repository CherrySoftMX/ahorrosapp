package com.cherrysoft.ahorrosapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class SavingsSummaryDTO {
  private List<DailySavingDTO> savings;
  private BigDecimal total;
  private BigDecimal average;
}
