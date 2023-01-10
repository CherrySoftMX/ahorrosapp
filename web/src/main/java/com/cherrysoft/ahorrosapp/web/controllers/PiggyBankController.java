package com.cherrysoft.ahorrosapp.web.controllers;

import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.core.models.specs.piggybank.UpdatePiggyBankSpec;
import com.cherrysoft.ahorrosapp.common.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.web.hateoas.assemblers.PiggyBankModelAssembler;
import com.cherrysoft.ahorrosapp.web.dtos.PiggyBankDTO;
import com.cherrysoft.ahorrosapp.web.dtos.validation.OnCreate;
import com.cherrysoft.ahorrosapp.web.mappers.PiggyBankMapper;
import com.cherrysoft.ahorrosapp.web.security.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static com.cherrysoft.ahorrosapp.web.utils.ApiDocsConstants.*;

@RestController
@Validated
@RequiredArgsConstructor
@Tag(name = "Piggy banks", description = "API to manage piggy banks")
@ApiResponses({
    @ApiResponse(ref = BAD_REQUEST_RESPONSE_REF, responseCode = "400"),
    @ApiResponse(ref = UNAUTHORIZED_RESPONSE_REF, responseCode = "401"),
    @ApiResponse(ref = INTERNAL_SERVER_ERROR_RESPONSE_REF, responseCode = "500")
})
public class PiggyBankController {
  private final PiggyBankMapper pbMapper;
  private final PiggyBankService pbService;
  private final PiggyBankModelAssembler pbModelAssembler;
  private final PagedResourcesAssembler<PiggyBank> pbPagedResourcesAssembler;

  @Operation(summary = "Returns the piggy banks associated with the logged user")
  @ApiResponse(responseCode = "200", description = "OK", content = {
      @Content(array = @ArraySchema(schema = @Schema(implementation = PiggyBankDTO.class)))
  })
  @GetMapping
  public PagedModel<PiggyBankDTO> getPiggyBanks(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PageableDefault @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
  ) {
    String ownerUsername = loggedUser.getUsername();
    Page<PiggyBank> result = pbService.getPiggyBanks(ownerUsername, pageable);
    return pbPagedResourcesAssembler.toModel(result, pbModelAssembler);
  }

  @Operation(summary = "Returns the piggy bank with the given name")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Piggy bank found", content = {
          @Content(schema = @Schema(implementation = PiggyBankDTO.class))
      }),
      @ApiResponse(ref = NOT_FOUND_RESPONSE_REF, responseCode = "404")
  })
  @GetMapping("/{pbName}")
  public PiggyBankDTO getPiggyBank(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String pbName
  ) {
    String ownerUsername = loggedUser.getUsername();
    PiggyBank result = pbService.getPiggyBankByName(ownerUsername, pbName);
    return pbModelAssembler.toModel(result);
  }

  @Operation(summary = "Creates and adds the given piggy bank to the logged user")
  @ApiResponse(responseCode = "201", description = "Piggy bank created", content = {
      @Content(schema = @Schema(implementation = PiggyBankDTO.class))
  })
  @PostMapping("/piggybank")
  @ResponseStatus(HttpStatus.CREATED)
  @Validated(OnCreate.class)
  public ResponseEntity<PiggyBankDTO> addPiggyBankTo(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @RequestBody @Valid PiggyBankDTO payload
  ) {
    String ownerUsername = loggedUser.getUsername();
    PiggyBank providedPb = pbMapper.toPiggyBank(payload);
    PiggyBank result = pbService.addPiggyBankTo(ownerUsername, providedPb);
    return ResponseEntity
        .created(URI.create(String.format("/%s", result.getName())))
        .body(pbModelAssembler.toModel(result));
  }

  @Operation(summary = "Partially updates the piggy bank with the given payload")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Piggy bank updated", content = {
          @Content(schema = @Schema(implementation = PiggyBankDTO.class))
      }),
      @ApiResponse(ref = NOT_FOUND_RESPONSE_REF, responseCode = "404")
  })
  @PatchMapping("/{pbName}")
  public PiggyBankDTO updatePiggyBank(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String pbName,
      @RequestBody @Valid PiggyBankDTO pbDto
  ) {
    String ownerUsername = loggedUser.getUsername();
    PiggyBank updatedPb = pbMapper.toPiggyBank(pbDto);
    var spec = new UpdatePiggyBankSpec(ownerUsername, pbName, updatedPb);
    PiggyBank result = pbService.partialUpdatePiggyBank(spec);
    return pbModelAssembler.toModel(result);
  }

  @Operation(summary = "Deletes a piggy bank with the given name")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Piggy bank deleted", content = {
          @Content(schema = @Schema(implementation = PiggyBankDTO.class))
      }),
      @ApiResponse(ref = NOT_FOUND_RESPONSE_REF, responseCode = "404")
  })
  @DeleteMapping("/{pbName}")
  public PiggyBankDTO deletePiggyBank(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String pbName
  ) {
    String ownerUsername = loggedUser.getUsername();
    PiggyBank deletedPb = pbService.deletePiggyBank(ownerUsername, pbName);
    return pbMapper.toPiggyBankDto(deletedPb);
  }

}
