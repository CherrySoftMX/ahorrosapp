package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.models.specs.piggybank.UpdatePiggyBankSpec;
import com.cherrysoft.ahorrosapp.dtos.PiggyBankDTO;
import com.cherrysoft.ahorrosapp.dtos.validation.OnCreate;
import com.cherrysoft.ahorrosapp.hateoas.assemblers.PiggyBankModelAssembler;
import com.cherrysoft.ahorrosapp.mappers.PiggyBankMapper;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(PiggyBankController.BASE_URL)
public class PiggyBankController {
  public static final String BASE_URL = "/{ownerUsername}";
  private final PiggyBankMapper pbMapper;
  private final PiggyBankService pbService;
  private final PiggyBankModelAssembler pbModelAssembler;
  private final PagedResourcesAssembler<PiggyBank> pbPagedResourcesAssembler;

  @GetMapping
  public PagedModel<PiggyBankDTO> getPiggyBanks(
      @PathVariable String ownerUsername,
      @PageableDefault @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
  ) {
    Page<PiggyBank> result = pbService.getPiggyBanks(ownerUsername, pageable);
    return pbPagedResourcesAssembler.toModel(result, pbModelAssembler);
  }

  @GetMapping("/{pbName}")
  public PiggyBankDTO getPiggyBank(
      @PathVariable String ownerUsername,
      @PathVariable String pbName
  ) {
    PiggyBank result = pbService.getPiggyBankByName(ownerUsername, pbName);
    return pbModelAssembler.toModel(result);
  }

  @PostMapping("/piggybank")
  @ResponseStatus(HttpStatus.CREATED)
  @Validated(OnCreate.class)
  public ResponseEntity<PiggyBankDTO> addPiggyBankTo(
      @PathVariable String ownerUsername,
      @RequestBody @Valid PiggyBankDTO pbDto
  ) {
    PiggyBank providedPb = pbMapper.toPiggyBank(pbDto);
    PiggyBank result = pbService.addPiggyBankTo(ownerUsername, providedPb);
    return ResponseEntity
        .created(URI.create(String.format("/%s/%s", ownerUsername, result.getName())))
        .body(pbModelAssembler.toModel(result));
  }

  @PatchMapping("/{pbName}")
  public PiggyBankDTO updatePiggyBank(
      @PathVariable String pbName,
      @PathVariable String ownerUsername,
      @RequestBody @Valid PiggyBankDTO pbDto
  ) {
    PiggyBank updatedPb = pbMapper.toPiggyBank(pbDto);
    var spec = new UpdatePiggyBankSpec(ownerUsername, pbName, updatedPb);
    PiggyBank result = pbService.partialUpdatePiggyBank(spec);
    return pbModelAssembler.toModel(result);
  }

  @DeleteMapping("/{pbName}")
  public PiggyBankDTO deletePiggyBank(
      @PathVariable String pbName,
      @PathVariable String ownerUsername
  ) {
    PiggyBank deletedPb = pbService.deletePiggyBank(ownerUsername, pbName);
    return pbMapper.toPiggyBankDto(deletedPb);
  }

}
