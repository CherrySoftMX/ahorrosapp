package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.core.PiggyBankSummary;
import com.cherrysoft.ahorrosapp.core.models.specs.SavingsSummarySpec;
import com.cherrysoft.ahorrosapp.dtos.summaries.IntervalSavingsSummaryDTO;
import com.cherrysoft.ahorrosapp.dtos.summaries.PiggyBankSummaryDTO;
import com.cherrysoft.ahorrosapp.hateoas.assemblers.IntervalSavingsSummaryAssembler;
import com.cherrysoft.ahorrosapp.hateoas.assemblers.PiggyBankSummaryAssembler;
import com.cherrysoft.ahorrosapp.security.SecurityUser;
import com.cherrysoft.ahorrosapp.services.SavingsSummaryService;
import com.cherrysoft.ahorrosapp.utils.ExtraMediaTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(SavingsSummaryController.BASE_URL)
public class SavingsSummaryController {
  public static final String BASE_URL = "/{pbName}/summary";
  private final SavingsSummaryService savingsSummaryService;
  private final PiggyBankSummaryAssembler pbSummaryAssembler;
  private final IntervalSavingsSummaryAssembler intervalSavingsSummaryAssembler;

  @GetMapping
  public IntervalSavingsSummaryDTO getIntervalSavingsSummaryAsJson(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String pbName,
      @RequestParam Map<String, String> summaryOptions
  ) {
    String ownerUsername = loggedUser.getUsername();
    var spec = new SavingsSummarySpec(ownerUsername, pbName, summaryOptions);
    var result = savingsSummaryService.getIntervalSavingsSummary(spec);
    return intervalSavingsSummaryAssembler.toModel(result);
  }

  @GetMapping(produces = ExtraMediaTypes.APPLICATION_EXCEL_VALUE)
  public ResponseEntity<ByteArrayResource> getIntervalSavingsSummaryAsXlsx(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String pbName,
      @RequestParam Map<String, String> summaryOptions
  ) {
    String ownerUsername = loggedUser.getUsername();
    String fileName = "test";
    var spec = new SavingsSummarySpec(ownerUsername, pbName, summaryOptions);
    byte[] result = savingsSummaryService.getIntervalSavingsSummaryAsXlsx(spec);
    return ResponseEntity
        .ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".xls\"")
        .contentType(MediaType.parseMediaType(ExtraMediaTypes.APPLICATION_EXCEL_VALUE))
        .cacheControl(CacheControl.noCache())
        .body(new ByteArrayResource(result));
  }

  @GetMapping("/total")
  public PiggyBankSummaryDTO getPiggyBankSummaryAsJson(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String pbName
  ) {
    String ownerUsername = loggedUser.getUsername();
    var spec = new SavingsSummarySpec(ownerUsername, pbName);
    PiggyBankSummary result = savingsSummaryService.getPiggyBankSummary(spec);
    return pbSummaryAssembler.toModel(result);
  }

}
