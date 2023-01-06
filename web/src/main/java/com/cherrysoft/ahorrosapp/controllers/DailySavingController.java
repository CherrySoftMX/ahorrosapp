package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.dtos.DailySavingDTO;
import com.cherrysoft.ahorrosapp.dtos.validation.OnCreate;
import com.cherrysoft.ahorrosapp.hateoas.assemblers.DailySavingModelAssembler;
import com.cherrysoft.ahorrosapp.mappers.DailySavingMapper;
import com.cherrysoft.ahorrosapp.security.SecurityUser;
import com.cherrysoft.ahorrosapp.services.DailySavingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

import static com.cherrysoft.ahorrosapp.core.models.DailySaving.DAY_MONTH_YEAR;

@RestController
@RequestMapping(DailySavingController.BASE_URL)
@Validated
@RequiredArgsConstructor
public class DailySavingController {
  public static final String BASE_URL = "/{pbName}/daily";
  private final DailySavingService dailySavingService;
  private final DailySavingMapper dailySavingMapper;
  private final DailySavingModelAssembler dailySavingModelAssembler;

  @GetMapping
  public DailySavingDTO getDailySaving(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String pbName,
      @RequestParam(required = false) @DateTimeFormat(pattern = DAY_MONTH_YEAR) LocalDate date
  ) {
    String ownerUsername = loggedUser.getUsername();
    var spec = new DailySavingSpec(ownerUsername, pbName, date);
    DailySaving result = dailySavingService.getDailySavingOrElseThrow(spec);
    return dailySavingModelAssembler.toModel(result);
  }

  @PostMapping
  @Validated(OnCreate.class)
  public ResponseEntity<DailySavingDTO> createDailySaving(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String pbName,
      @RequestParam(required = false) @DateTimeFormat(pattern = DAY_MONTH_YEAR) LocalDate date,
      @RequestBody @Valid DailySavingDTO payload
  ) {
    String ownerUsername = loggedUser.getUsername();
    DailySaving dailySaving = dailySavingMapper.toDailySaving(payload).setDate(date);
    var spec = new DailySavingSpec(ownerUsername, pbName, dailySaving);
    DailySaving result = dailySavingService.createDailySaving(spec);
    return ResponseEntity.ok(dailySavingModelAssembler.toModel(result));
  }

  @PatchMapping
  public DailySavingDTO updateDailySaving(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String pbName,
      @RequestParam(required = false) @DateTimeFormat(pattern = DAY_MONTH_YEAR) LocalDate date,
      @RequestBody @Valid DailySavingDTO payload
  ) {
    String ownerUsername = loggedUser.getUsername();
    DailySaving updatedDailySaving = dailySavingMapper.toDailySaving(payload).setDate(date);
    var spec = new DailySavingSpec(ownerUsername, pbName, updatedDailySaving);
    DailySaving result = dailySavingService.updateDailySaving(spec);
    return dailySavingModelAssembler.toModel(result);
  }

  @DeleteMapping
  public DailySavingDTO deleteDailySaving(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String pbName,
      @RequestParam(required = false) @DateTimeFormat(pattern = DAY_MONTH_YEAR) LocalDate date
  ) {
    String ownerUsername = loggedUser.getUsername();
    var spec = new DailySavingSpec(ownerUsername, pbName, date);
    DailySaving deletedDailySaving = dailySavingService.deleteDailySaving(spec);
    return dailySavingMapper.toDailySavingDto(deletedDailySaving);
  }

}
