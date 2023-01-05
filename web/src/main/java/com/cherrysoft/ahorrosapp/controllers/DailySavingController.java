package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.dtos.DailySavingDTO;
import com.cherrysoft.ahorrosapp.dtos.validation.OnCreate;
import com.cherrysoft.ahorrosapp.mappers.DailySavingMapper;
import com.cherrysoft.ahorrosapp.services.DailySavingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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
  public static final String BASE_URL = "{ownerUsername}/{pbName}/daily";
  private final DailySavingService dailySavingService;
  private final DailySavingMapper dailySavingMapper;

  @GetMapping
  public ResponseEntity<DailySavingDTO> getDailySaving(
      @PathVariable String ownerUsername,
      @PathVariable String pbName,
      @RequestParam(required = false) @DateTimeFormat(pattern = DAY_MONTH_YEAR) LocalDate date
  ) {
    var spec = new DailySavingSpec(ownerUsername, pbName, date);
    DailySaving result = dailySavingService.getDailySavingOrElseThrow(spec);
    return ResponseEntity.ok(dailySavingMapper.toDailySavingDto(result));
  }

  @PostMapping
  @Validated(OnCreate.class)
  public ResponseEntity<DailySavingDTO> createDailySaving(
      @PathVariable String ownerUsername,
      @PathVariable String pbName,
      @RequestParam(required = false) @DateTimeFormat(pattern = DAY_MONTH_YEAR) LocalDate date,
      @RequestBody @Valid DailySavingDTO payload
  ) {
    DailySaving dailySaving = dailySavingMapper.toDailySaving(payload).setDate(date);
    var spec = new DailySavingSpec(ownerUsername, pbName, dailySaving);
    DailySaving result = dailySavingService.createDailySaving(spec);
    return ResponseEntity.ok(dailySavingMapper.toDailySavingDto(result));
  }

  @PatchMapping
  public ResponseEntity<DailySavingDTO> updateDailySaving(
      @PathVariable String ownerUsername,
      @PathVariable String pbName,
      @RequestParam(required = false) @DateTimeFormat(pattern = DAY_MONTH_YEAR) LocalDate date,
      @RequestBody @Valid DailySavingDTO payload
  ) {
    DailySaving updatedDailySaving = dailySavingMapper.toDailySaving(payload).setDate(date);
    var spec = new DailySavingSpec(ownerUsername, pbName, updatedDailySaving);
    DailySaving result = dailySavingService.updateDailySaving(spec);
    return ResponseEntity.ok(dailySavingMapper.toDailySavingDto(result));
  }

  @DeleteMapping
  public ResponseEntity<DailySavingDTO> deleteDailySaving(
      @PathVariable String ownerUsername,
      @PathVariable String pbName,
      @RequestParam(required = false) @DateTimeFormat(pattern = DAY_MONTH_YEAR) LocalDate date
  ) {
    var spec = new DailySavingSpec(ownerUsername, pbName, date);
    DailySaving deletedDailySaving = dailySavingService.deleteDailySaving(spec);
    return ResponseEntity.ok(dailySavingMapper.toDailySavingDto(deletedDailySaving));
  }

}
