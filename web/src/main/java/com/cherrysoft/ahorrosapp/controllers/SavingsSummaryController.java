package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.core.models.specs.SavingsSummarySpec;
import com.cherrysoft.ahorrosapp.dtos.summaries.IntervalSavingsSummaryDTO;
import com.cherrysoft.ahorrosapp.dtos.summaries.PiggyBankSummaryDTO;
import com.cherrysoft.ahorrosapp.mappers.SavingsSummaryMapper;
import com.cherrysoft.ahorrosapp.services.SavingsSummaryService;
import com.cherrysoft.ahorrosapp.utils.ExtraMediaTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(SavingsSummaryController.BASE_URL)
public class SavingsSummaryController {
  public static final String BASE_URL = "{ownerUsername}/{pbName}/summary";
  private final SavingsSummaryService savingsSummaryService;
  private final SavingsSummaryMapper savingsSummaryMapper;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<IntervalSavingsSummaryDTO> getIntervalSavingsSummaryAsJson(
      @PathVariable String ownerUsername,
      @PathVariable String pbName,
      @RequestParam Map<String, String> summaryOptions
  ) {
    var spec = new SavingsSummarySpec(ownerUsername, pbName, summaryOptions);
    var savingsSummary = savingsSummaryService.getIntervalSavingsSummary(spec);
    var savingsSummaryDto = savingsSummaryMapper.toIntervalSavingsSummaryDto(savingsSummary);
    return ResponseEntity.ok(savingsSummaryDto);
  }

  @GetMapping(produces = ExtraMediaTypes.APPLICATION_EXCEL_VALUE)
  public ResponseEntity<ByteArrayResource> getIntervalSavingsSummaryAsXlsx(
      @PathVariable String ownerUsername,
      @PathVariable String pbName,
      @RequestParam Map<String, String> summaryOptions
  ) {
    String fileName = "test";
    var spec = new SavingsSummarySpec(ownerUsername, pbName, summaryOptions);
    return ResponseEntity
        .ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".xls\"")
        .contentType(MediaType.parseMediaType(ExtraMediaTypes.APPLICATION_EXCEL_VALUE))
        .cacheControl(CacheControl.noCache())
        .body(new ByteArrayResource(savingsSummaryService.getIntervalSavingsSummaryAsXlsx(spec)));
  }

  @GetMapping("/total")
  public ResponseEntity<PiggyBankSummaryDTO> getPiggyBankSummary(
      @PathVariable String ownerUsername,
      @PathVariable String pbName
  ) {
    var spec = new SavingsSummarySpec(ownerUsername, pbName);
    var pbSummary = savingsSummaryService.getPiggyBankSummary(spec);
    return ResponseEntity.ok(savingsSummaryMapper.toPiggyBankSummaryDto(pbSummary));
  }

}
