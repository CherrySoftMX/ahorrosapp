package com.cherrysoft.ahorrosapp.web.controllers.summaries;

import com.cherrysoft.ahorrosapp.common.core.PiggyBankSummary;
import com.cherrysoft.ahorrosapp.common.core.models.specs.SavingsSummarySpec;
import com.cherrysoft.ahorrosapp.common.services.SavingsSummaryService;
import com.cherrysoft.ahorrosapp.web.dtos.summaries.PiggyBankSummaryDTO;
import com.cherrysoft.ahorrosapp.web.hateoas.assemblers.PiggyBankSummaryAssembler;
import com.cherrysoft.ahorrosapp.web.security.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cherrysoft.ahorrosapp.web.utils.ApiDocsConstants.*;

@RestController
@RequestMapping(PiggyBankSummaryController.BASE_URL)
@RequiredArgsConstructor
@Tag(name = "Piggy bank summary", description = "API to get piggy bank summaries")
@ApiResponses({
    @ApiResponse(ref = BAD_REQUEST_RESPONSE_REF, responseCode = "400"),
    @ApiResponse(ref = UNAUTHORIZED_RESPONSE_REF, responseCode = "401"),
    @ApiResponse(ref = NOT_FOUND_RESPONSE_REF, responseCode = "404"),
    @ApiResponse(ref = INTERNAL_SERVER_ERROR_RESPONSE_REF, responseCode = "500")
})
public class PiggyBankSummaryController {
  public static final String BASE_URL = "/{pbName}/summary";
  private final SavingsSummaryService savingsSummaryService;
  private final PiggyBankSummaryAssembler pbSummaryAssembler;

  @Operation(summary = "Returns the piggy bank summary of the specified piggy bank")
  @ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(schema = @Schema(implementation = PiggyBankSummaryDTO.class))
  })
  @GetMapping
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
