package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.core.queryparams.SavingsSummaryQueryParams;
import com.cherrysoft.ahorrosapp.dtos.summaries.IntervalSavingsSummaryDTO;
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

  @GetMapping
  public ResponseEntity<IntervalSavingsSummaryDTO> getIntervalSavingsSummary(
      @PathVariable String ownerUsername,
      @PathVariable String pbName,
      @RequestParam Map<String, String> summaryOptions
  ) {
    var params = new SavingsSummaryQueryParams(ownerUsername, pbName, summaryOptions);
    var savingsSummary = savingsSummaryService.getIntervalSavingsSummary(params);
    return ResponseEntity.ok(savingsSummaryMapper.toIntervalSavingsSummaryDto(savingsSummary));
  }

  @GetMapping("/total")
  public ResponseEntity<PiggyBankSummaryDTO> getPiggyBankSummary(
      @PathVariable String ownerUsername,
      @PathVariable String pbName
  ) {
    var params = new SavingsSummaryQueryParams(ownerUsername, pbName);
    var pbSummary = savingsSummaryService.getPiggyBankSummary(params);
    return ResponseEntity.ok(savingsSummaryMapper.toPiggyBankSummaryDto(pbSummary));
  }

}