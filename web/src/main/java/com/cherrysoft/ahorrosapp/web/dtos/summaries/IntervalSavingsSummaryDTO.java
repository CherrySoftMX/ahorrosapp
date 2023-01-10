package com.cherrysoft.ahorrosapp.web.dtos.summaries;

import com.cherrysoft.ahorrosapp.web.dtos.DailySavingDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class IntervalSavingsSummaryDTO extends RepresentationModel<IntervalSavingsSummaryDTO> {
  private final BigDecimal totalAmount;
  private final BigDecimal averageAmount;
  private final List<DailySavingDTO> savings;
}
