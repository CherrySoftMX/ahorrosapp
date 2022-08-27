package com.cherrysoft.ahorrosapp.controllers.summaries.reponsestrategies;

import com.cherrysoft.ahorrosapp.core.queryparams.SavingsSummaryQueryParams;
import com.cherrysoft.ahorrosapp.mappers.SavingsSummaryMapper;
import com.cherrysoft.ahorrosapp.services.SavingsSummaryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
public abstract class SummaryResponseStrategy {
  protected final SavingsSummaryQueryParams params;
  protected final SavingsSummaryService summaryService;
  protected final SavingsSummaryMapper summaryMapper;

  public abstract ResponseEntity<Object> formatResponse();

}
