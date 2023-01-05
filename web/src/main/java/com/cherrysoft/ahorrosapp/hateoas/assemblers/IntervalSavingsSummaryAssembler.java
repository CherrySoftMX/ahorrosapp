package com.cherrysoft.ahorrosapp.hateoas.assemblers;

import com.cherrysoft.ahorrosapp.core.IntervalSavingsSummary;
import com.cherrysoft.ahorrosapp.dtos.DailySavingDTO;
import com.cherrysoft.ahorrosapp.dtos.summaries.IntervalSavingsSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class IntervalSavingsSummaryAssembler implements RepresentationModelAssembler<IntervalSavingsSummary, IntervalSavingsSummaryDTO> {
  private final DailySavingModelAssembler dailySavingModelAssembler;

  @Override
  public IntervalSavingsSummaryDTO toModel(IntervalSavingsSummary intervalSavingsSummary) {
    CollectionModel<DailySavingDTO> savings = dailySavingModelAssembler.toCollectionModel(intervalSavingsSummary.getSavings());
    return new IntervalSavingsSummaryDTO(
        intervalSavingsSummary.getTotalAmount(),
        intervalSavingsSummary.getAverageAmount(),
        new ArrayList<>(savings.getContent())
    );
  }

}
