package com.cherrysoft.ahorrosapp.controllers.summaries;

import com.cherrysoft.ahorrosapp.controllers.summaries.factories.SummaryResponseFormatFactory;
import com.cherrysoft.ahorrosapp.core.params.SavingsSummaryParams;
import com.cherrysoft.ahorrosapp.dtos.summaries.PiggyBankSummaryDTO;
import com.cherrysoft.ahorrosapp.mappers.SavingsSummaryMapper;
import com.cherrysoft.ahorrosapp.services.SavingsSummaryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping(SavingsSummaryController.BASE_URL)
public class SavingsSummaryController {
  public static final String BASE_URL = "{ownerUsername}/{pbName}/summary";
  private final SavingsSummaryService savingsSummaryService;
  private final SavingsSummaryMapper savingsSummaryMapper;
  private final SummaryResponseFormatFactory summaryResponseFormatFactory;

  @GetMapping
  public ResponseEntity<Object> intervalSavingsSummary(
      @PathVariable String ownerUsername,
      @PathVariable String pbName,
      @RequestParam Map<String, String> summaryOptions
  ) {
    var params = new SavingsSummaryParams(ownerUsername, pbName, summaryOptions);
    var formatStrategy = summaryResponseFormatFactory.createSummaryResponseStrategy(params);
    return formatStrategy.formatResponse();
  }

  @GetMapping("/total")
  public ResponseEntity<PiggyBankSummaryDTO> piggyBankSummary(
      @PathVariable String ownerUsername,
      @PathVariable String pbName
  ) {
    var params = new SavingsSummaryParams(ownerUsername, pbName);
    var pbSummary = savingsSummaryService.getPiggyBankSummary(params);
    return ResponseEntity.ok(savingsSummaryMapper.toPiggyBankSummaryDto(pbSummary));
  }

}
