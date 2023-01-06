package com.cherrysoft.ahorrosapp.dtos.summaries;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class PiggyBankSummaryDTO extends RepresentationModel<PiggyBankSummaryDTO> {
  private final BigDecimal initialAmount;
  private final BigDecimal totalAmount;
  private final BigDecimal averageAmount;
  private final BigDecimal inHandAmount;
  private final BigDecimal borrowedAmount;
}
