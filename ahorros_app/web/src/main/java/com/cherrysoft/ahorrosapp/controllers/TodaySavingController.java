package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.dtos.DailySavingDTO;
import com.cherrysoft.ahorrosapp.dtos.TodaySavingDTO;
import com.cherrysoft.ahorrosapp.dtos.validation.OnCreate;
import com.cherrysoft.ahorrosapp.mappers.DailySavingMapper;
import com.cherrysoft.ahorrosapp.models.DailySaving;
import com.cherrysoft.ahorrosapp.services.TodaySavingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(TodaySavingController.BASE_URL)
public class TodaySavingController {
  public static final String BASE_URL = "{ownerUsername}/{pbName}/today";
  private final DailySavingMapper dailySavingMapper;
  private final TodaySavingService todaySavingService;

  @GetMapping
  public ResponseEntity<DailySavingDTO> todaySaving(
      @PathVariable String ownerUsername,
      @PathVariable String pbName
  ) {
    DailySaving todaySaving = todaySavingService.getTodaySavingOrThrowIfNotPresent(ownerUsername, pbName);
    return ResponseEntity.ok(dailySavingMapper.toDailySavingDto(todaySaving));
  }

  @PostMapping
  @Validated(OnCreate.class)
  public ResponseEntity<DailySavingDTO> createTodaySaving(
      @PathVariable String ownerUsername,
      @PathVariable String pbName,
      @RequestBody @Valid TodaySavingDTO todaySavingDto
  ) {
    DailySaving providedTodaySaving = dailySavingMapper.toDailySaving(todaySavingDto);
    DailySaving createdTodaySaving = todaySavingService.createTodaySaving(ownerUsername, pbName, providedTodaySaving);
    return ResponseEntity.ok(dailySavingMapper.toDailySavingDto(createdTodaySaving));
  }

  @PatchMapping
  public ResponseEntity<DailySavingDTO> partialUpdateTodaySaving(
      @PathVariable String ownerUsername,
      @PathVariable String pbName,
      @RequestBody @Valid TodaySavingDTO todaySavingDTO
  ) {
    DailySaving partialUpdatedTodaySaving = dailySavingMapper.toDailySaving(todaySavingDTO);
    DailySaving updatedTodaySaving = todaySavingService.partialUpdateTodaySaving(ownerUsername, pbName, partialUpdatedTodaySaving);
    return ResponseEntity.ok(dailySavingMapper.toDailySavingDto(updatedTodaySaving));
  }

  @DeleteMapping
  public ResponseEntity<DailySavingDTO> deleteTodaySaving(
      @PathVariable String ownerUsername,
      @PathVariable String pbName
  ) {
    DailySaving deletedTodaySaving = todaySavingService.deleteTodaySaving(ownerUsername, pbName);
    return ResponseEntity.ok(dailySavingMapper.toDailySavingDto(deletedTodaySaving));
  }

}
