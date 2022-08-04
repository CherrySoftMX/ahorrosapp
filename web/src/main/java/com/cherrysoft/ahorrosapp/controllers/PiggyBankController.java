package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.dtos.PiggyBankDTO;
import com.cherrysoft.ahorrosapp.dtos.validation.OnCreate;
import com.cherrysoft.ahorrosapp.mappers.PiggyBankMapper;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(PiggyBankController.BASE_URL)
public class PiggyBankController {
  public static final String BASE_URL = "/{ownerUsername}";
  private final PiggyBankMapper pbMapper;
  private final PiggyBankService pbService;

  @PostMapping("/piggybank")
  @ResponseStatus(HttpStatus.CREATED)
  @Validated(OnCreate.class)
  public ResponseEntity<PiggyBankDTO> addPiggyBankTo(
      @PathVariable String ownerUsername,
      @RequestBody @Valid PiggyBankDTO pbDto
  ) throws URISyntaxException {
    PiggyBank providedPb = pbMapper.toPiggyBank(pbDto);
    PiggyBank addedPb = pbService.addPiggyBankTo(providedPb, ownerUsername);
    return ResponseEntity
        .created(new URI(String.format("/%s/%s", ownerUsername, addedPb.getName())))
        .body(pbMapper.toPiggyBankDto(addedPb));
  }

  @PatchMapping("/{pbName}")
  public ResponseEntity<PiggyBankDTO> partialUpdatePiggyBank(
      @PathVariable String pbName,
      @PathVariable String ownerUsername,
      @RequestBody @Valid PiggyBankDTO pbDto
  ) {
    PiggyBank partialUpdatedPb = pbMapper.toPiggyBank(pbDto);
    PiggyBank updatedPb = pbService.partialUpdatePiggyBank(ownerUsername, pbName, partialUpdatedPb);
    return ResponseEntity.ok(pbMapper.toPiggyBankDto(updatedPb));
  }

  @DeleteMapping("/{pbName}")
  public ResponseEntity<PiggyBankDTO> deletePiggyBank(
      @PathVariable String pbName,
      @PathVariable String ownerUsername
  ) {
    PiggyBank deletedPb = pbService.deletePiggyBank(pbName, ownerUsername);
    return ResponseEntity.ok(pbMapper.toPiggyBankDto(deletedPb));
  }
}
