package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.models.specs.piggybank.UpdatePiggyBankSpec;
import com.cherrysoft.ahorrosapp.dtos.PiggyBankDTO;
import com.cherrysoft.ahorrosapp.dtos.validation.OnCreate;
import com.cherrysoft.ahorrosapp.hateoas.assemblers.PiggyBankModelAssembler;
import com.cherrysoft.ahorrosapp.mappers.PiggyBankMapper;
import com.cherrysoft.ahorrosapp.security.SecurityUser;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;
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

@RestController
@Validated
@RequiredArgsConstructor
public class PiggyBankController {
  private final PiggyBankMapper pbMapper;
  private final PiggyBankService pbService;
  private final PiggyBankModelAssembler pbModelAssembler;
  private final PagedResourcesAssembler<PiggyBank> pbPagedResourcesAssembler;

  @GetMapping
  public PagedModel<PiggyBankDTO> getPiggyBanks(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PageableDefault @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
  ) {
    String ownerUsername = loggedUser.getUsername();
    Page<PiggyBank> result = pbService.getPiggyBanks(ownerUsername, pageable);
    return pbPagedResourcesAssembler.toModel(result, pbModelAssembler);
  }

  @GetMapping("/{pbName}")
  public PiggyBankDTO getPiggyBank(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String pbName
  ) {
    String ownerUsername = loggedUser.getUsername();
    PiggyBank result = pbService.getPiggyBankByName(ownerUsername, pbName);
    return pbModelAssembler.toModel(result);
  }

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
