package com.cherrysoft.ahorrosapp.web.controllers;

import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.common.services.DailySavingService;
import com.cherrysoft.ahorrosapp.web.dtos.DailySavingDTO;
import com.cherrysoft.ahorrosapp.web.dtos.UserDTO;
import com.cherrysoft.ahorrosapp.web.dtos.validation.OnCreate;
import com.cherrysoft.ahorrosapp.web.hateoas.assemblers.DailySavingModelAssembler;
import com.cherrysoft.ahorrosapp.web.mappers.DailySavingMapper;
import com.cherrysoft.ahorrosapp.web.security.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

import static com.cherrysoft.ahorrosapp.common.utils.DateUtils.DAY_MONTH_YEAR_PATTERN;
import static com.cherrysoft.ahorrosapp.web.utils.ApiDocsConstants.*;

@RestController
@RequestMapping(DailySavingController.BASE_URL)
@Validated
@RequiredArgsConstructor
@Tag(name = "Daily savings", description = "API to manage daily savings")
@ApiResponses({
    @ApiResponse(ref = BAD_REQUEST_RESPONSE_REF, responseCode = "400"),
    @ApiResponse(ref = UNAUTHORIZED_RESPONSE_REF, responseCode = "401"),
    @ApiResponse(ref = INTERNAL_SERVER_ERROR_RESPONSE_REF, responseCode = "500")
})
public class DailySavingController {
  public static final String BASE_URL = "/{pbName}/daily";
  private final DailySavingService dailySavingService;
  private final DailySavingMapper dailySavingMapper;
  private final DailySavingModelAssembler dailySavingModelAssembler;

  @Operation(summary = "Returns the saving with the given date or today's saving if not date provided.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Saving found", content = {
          @Content(schema = @Schema(implementation = UserDTO.class))
      }),
      @ApiResponse(ref = NOT_FOUND_RESPONSE_REF, responseCode = "404")
  })
  @GetMapping
  public DailySavingDTO getDailySaving(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String pbName,
      @RequestParam(required = false) @DateTimeFormat(pattern = DAY_MONTH_YEAR_PATTERN) LocalDate date
  ) {
    String ownerUsername = loggedUser.getUsername();
    var spec = new DailySavingSpec(ownerUsername, pbName, date);
    DailySaving result = dailySavingService.getDailySavingOrElseThrow(spec);
    return dailySavingModelAssembler.toModel(result);
  }

  @Operation(summary = "Creates a saving in the given date or today's date if no date is provided. If a saving already exists on the specified date, the saving will be overwritten")
  @ApiResponse(responseCode = "200", description = "Saving updated", content = {
      @Content(schema = @Schema(implementation = UserDTO.class))
  })
  @PostMapping
  @Validated(OnCreate.class)
  public ResponseEntity<DailySavingDTO> createDailySaving(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String pbName,
      @RequestParam(required = false) @DateTimeFormat(pattern = DAY_MONTH_YEAR_PATTERN) LocalDate date,
      @RequestBody @Valid DailySavingDTO payload
  ) {
    String ownerUsername = loggedUser.getUsername();
    DailySaving dailySaving = dailySavingMapper.toDailySaving(payload).setDate(date);
    var spec = new DailySavingSpec(ownerUsername, pbName, dailySaving);
    DailySaving result = dailySavingService.createDailySaving(spec);
    return ResponseEntity.ok(dailySavingModelAssembler.toModel(result));
  }

  @Operation(summary = "Partially updates the saving with the given date or today's savings if no date is provided.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Saving updated", content = {
          @Content(schema = @Schema(implementation = UserDTO.class))
      }),
      @ApiResponse(ref = NOT_FOUND_RESPONSE_REF, responseCode = "404")
  })
  @PatchMapping
  public DailySavingDTO updateDailySaving(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String pbName,
      @RequestParam(required = false) @DateTimeFormat(pattern = DAY_MONTH_YEAR_PATTERN) LocalDate date,
      @RequestBody @Valid DailySavingDTO payload
  ) {
    String ownerUsername = loggedUser.getUsername();
    DailySaving updatedDailySaving = dailySavingMapper.toDailySaving(payload).setDate(date);
    var spec = new DailySavingSpec(ownerUsername, pbName, updatedDailySaving);
    DailySaving result = dailySavingService.updateDailySaving(spec);
    return dailySavingModelAssembler.toModel(result);
  }

  @Operation(summary = "Deletes the saving with the given date or today's savings if no date is provided.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Saving deleted", content = {
          @Content(schema = @Schema(implementation = UserDTO.class))
      }),
      @ApiResponse(ref = NOT_FOUND_RESPONSE_REF, responseCode = "404")
  })
  @DeleteMapping
  public DailySavingDTO deleteDailySaving(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String pbName,
      @RequestParam(required = false) @DateTimeFormat(pattern = DAY_MONTH_YEAR_PATTERN) LocalDate date
  ) {
    String ownerUsername = loggedUser.getUsername();
    var spec = new DailySavingSpec(ownerUsername, pbName, date);
    DailySaving deletedDailySaving = dailySavingService.deleteDailySaving(spec);
    return dailySavingMapper.toDailySavingDto(deletedDailySaving);
  }

}
