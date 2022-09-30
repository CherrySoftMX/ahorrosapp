package com.cherrysoft.ahorrosapp.controllers.summaries.reponsestrategies;

import com.cherrysoft.ahorrosapp.core.params.SavingsSummaryParams;
import com.cherrysoft.ahorrosapp.mappers.SavingsSummaryMapper;
import com.cherrysoft.ahorrosapp.services.SavingsSummaryService;
import org.springframework.http.ResponseEntity;

public class JsonSummaryResponseStrategy extends SummaryResponseStrategy {

  public JsonSummaryResponseStrategy(
      SavingsSummaryParams params,
      SavingsSummaryService summaryService,
      SavingsSummaryMapper summaryMapper
  ) {
    super(params, summaryService, summaryMapper);
  }

  @Override
  public ResponseEntity<Object> formatResponse() {
    var savingsSummary = summaryService.getIntervalSavingsSummary(params);
    var savingsSummaryDTO = summaryMapper.toIntervalSavingsSummaryDto(savingsSummary);
    return ResponseEntity.ok(savingsSummaryDTO);
  }

}
