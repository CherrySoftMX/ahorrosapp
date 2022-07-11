package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.core.SavingsSummary;
import com.cherrysoft.ahorrosapp.core.queryparams.SavingsSummaryQueryParams;
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

  @GetMapping
  public ResponseEntity<SavingsSummary> getSavingsSummary(
      @PathVariable String ownerUsername,
      @PathVariable String pbName,
      @RequestParam Map<String, String> summaryOptions
  ) {
    var params = new SavingsSummaryQueryParams(ownerUsername, pbName, summaryOptions);
    SavingsSummary savingsSummary = savingsSummaryService.getSavingsSummary(params);
    return ResponseEntity.ok(savingsSummary);
  }

}
