package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.params.DailySavingParams;
import com.cherrysoft.ahorrosapp.dtos.DailySavingDTO;
import com.cherrysoft.ahorrosapp.dtos.validation.OnCreate;
import com.cherrysoft.ahorrosapp.mappers.DailySavingMapper;
import com.cherrysoft.ahorrosapp.services.DailySavingService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

import static com.cherrysoft.ahorrosapp.core.models.DailySaving.DAY_MONTH_YEAR;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(DailySavingController.BASE_URL)
public class DailySavingController {
  public static final String BASE_URL = "{ownerUsername}/{pbName}/{date}";
  private final DailySavingService dailySavingService;
  private final DailySavingMapper dailySavingMapper;

  @GetMapping
  public ResponseEntity<DailySavingDTO> dailySaving(
      @PathVariable String ownerUsername,
      @PathVariable String pbName,
      @PathVariable @DateTimeFormat(pattern = DAY_MONTH_YEAR) LocalDate date
  ) {
    var params = new DailySavingParams(ownerUsername, pbName, date);
    DailySaving dailySaving = dailySavingService.getDailySavingOrThrowIfNotPresent(params);
    return ResponseEntity.ok(dailySavingMapper.toDailySavingDto(dailySaving));
  }

  @PostMapping
  @Validated(OnCreate.class)
  public ResponseEntity<DailySavingDTO> createDailySaving(
      @PathVariable String ownerUsername,
      @PathVariable String pbName,
      @PathVariable @DateTimeFormat(pattern = DAY_MONTH_YEAR) LocalDate date,
      @RequestBody @Valid DailySavingDTO dailySavingDTO
  ) {
    var params = new DailySavingParams(ownerUsername, pbName, date);
    DailySaving providedDailySaving = dailySavingMapper.toDailySaving(dailySavingDTO);
    DailySaving createdDailySaving = dailySavingService.createDailySaving(params, providedDailySaving);
    return ResponseEntity.ok(dailySavingMapper.toDailySavingDto(createdDailySaving));
  }

  @PatchMapping
  public ResponseEntity<DailySavingDTO> partialUpdateDailySaving(
      @PathVariable String ownerUsername,
      @PathVariable String pbName,
      @PathVariable @DateTimeFormat(pattern = DAY_MONTH_YEAR) LocalDate date,
      @RequestBody @Valid DailySavingDTO dailySavingDTO
  ) {
    var params = new DailySavingParams(ownerUsername, pbName, date);
    DailySaving partialUpdatedDailySaving = dailySavingMapper.toDailySaving(dailySavingDTO);
    DailySaving updatedDailySaving = dailySavingService.partialUpdateDailySaving(params, partialUpdatedDailySaving);
    return ResponseEntity.ok(dailySavingMapper.toDailySavingDto(updatedDailySaving));
  }

  @DeleteMapping
  public ResponseEntity<DailySavingDTO> deleteDailySaving(
      @PathVariable String ownerUsername,
      @PathVariable String pbName,
      @PathVariable @DateTimeFormat(pattern = DAY_MONTH_YEAR) LocalDate date
  ) {
    var params = new DailySavingParams(ownerUsername, pbName, date);
    DailySaving deletedDailySaving = dailySavingService.deleteDailySaving(params);
    return ResponseEntity.ok(dailySavingMapper.toDailySavingDto(deletedDailySaving));
  }

}
