package com.cherrysoft.ahorrosapp.web.controllers.summaries;

import com.cherrysoft.ahorrosapp.common.core.IntervalSavingsSummary;
import com.cherrysoft.ahorrosapp.common.core.models.ExcelReportResult;
import com.cherrysoft.ahorrosapp.common.core.models.specs.SavingsSummarySpec;
import com.cherrysoft.ahorrosapp.common.services.SavingsSummaryService;
import com.cherrysoft.ahorrosapp.web.controllers.summaries.utils.ExcelDocumentResponseBuilder;
import com.cherrysoft.ahorrosapp.web.dtos.summaries.IntervalSavingsSummaryDTO;
import com.cherrysoft.ahorrosapp.web.hateoas.assemblers.IntervalSavingsSummaryAssembler;
import com.cherrysoft.ahorrosapp.web.security.SecurityUser;
import com.cherrysoft.ahorrosapp.web.utils.ExtraMediaTypes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.cherrysoft.ahorrosapp.common.utils.DateUtils.MONTH_YEAR_PATTERN;
import static com.cherrysoft.ahorrosapp.web.utils.ApiDocsConstants.*;

@RestController
@RequestMapping(IntervalMonthSummaryController.BASE_URL)
@RequiredArgsConstructor
@Tag(name = "Interval month summary", description = "API to get interval month summaries from a piggy bank")
@ApiResponses({
    @ApiResponse(ref = BAD_REQUEST_RESPONSE_REF, responseCode = "400"),
    @ApiResponse(ref = UNAUTHORIZED_RESPONSE_REF, responseCode = "401"),
    @ApiResponse(ref = NOT_FOUND_RESPONSE_REF, responseCode = "404"),
    @ApiResponse(ref = INTERNAL_SERVER_ERROR_RESPONSE_REF, responseCode = "500")
})
public class IntervalMonthSummaryController {
  public static final String BASE_URL = "/{pbName}/summary/interval";
  private final SavingsSummaryService savingsSummaryService;
  private final IntervalSavingsSummaryAssembler intervalSavingsSummaryAssembler;

  @Operation(summary = "Returns a interval month summary of the specified piggy bank and range months")
  @ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = IntervalSavingsSummaryDTO.class))
  })
  @GetMapping
  public IntervalSavingsSummaryDTO getIntervalMonthSummaryAsJson(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String pbName,
      @RequestParam @DateTimeFormat(pattern = MONTH_YEAR_PATTERN) String start,
      @RequestParam @DateTimeFormat(pattern = MONTH_YEAR_PATTERN) String end
  ) {
    String ownerUsername = loggedUser.getUsername();
    var spec = new SavingsSummarySpec(ownerUsername, pbName, Map.of("start", start, "end", end, "type", "interval-month"));
    IntervalSavingsSummary result = savingsSummaryService.getIntervalSavingsSummary(spec);
    return intervalSavingsSummaryAssembler.toModel(result);
  }

  @ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = ByteArrayResource.class))
  })
  @GetMapping(produces = ExtraMediaTypes.APPLICATION_EXCEL_VALUE)
  public ResponseEntity<ByteArrayResource> getIntervalMonthSummaryAsXlsx(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String pbName,
      @RequestParam @DateTimeFormat(pattern = MONTH_YEAR_PATTERN) String start,
      @RequestParam @DateTimeFormat(pattern = MONTH_YEAR_PATTERN) String end
  ) {
    String ownerUsername = loggedUser.getUsername();
    var spec = new SavingsSummarySpec(ownerUsername, pbName, Map.of("start", start, "end", end, "type", "interval-month"));
    ExcelReportResult result = savingsSummaryService.getIntervalSavingsSummaryAsXlsx(spec);
    return ExcelDocumentResponseBuilder.from(result);
  }

}
