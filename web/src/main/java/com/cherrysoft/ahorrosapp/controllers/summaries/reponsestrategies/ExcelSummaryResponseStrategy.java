package com.cherrysoft.ahorrosapp.controllers.summaries.reponsestrategies;

import com.cherrysoft.ahorrosapp.core.queryparams.SavingsSummaryQueryParams;
import com.cherrysoft.ahorrosapp.mappers.SavingsSummaryMapper;
import com.cherrysoft.ahorrosapp.services.SavingsSummaryService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ExcelSummaryResponseStrategy extends SummaryResponseStrategy {

  public ExcelSummaryResponseStrategy(
      SavingsSummaryQueryParams params,
      SavingsSummaryService summaryService,
      SavingsSummaryMapper summaryMapper
  ) {
    super(params, summaryService, summaryMapper);
  }

  @Override
  public ResponseEntity<Object> formatResponse() {
    String fileName = "test";
    return ResponseEntity
        .ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".xls\"")
        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
        .cacheControl(CacheControl.noCache())
        .body(new ByteArrayResource(summaryService.getSavingsSummaryAsXlsxFile(params)));
  }

}
