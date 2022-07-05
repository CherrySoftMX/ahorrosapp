package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.dtos.DailySavingDTO;
import com.cherrysoft.ahorrosapp.dtos.validation.OnCreate;
import com.cherrysoft.ahorrosapp.mappers.DailySavingMapper;
import com.cherrysoft.ahorrosapp.models.DailySaving;
import com.cherrysoft.ahorrosapp.services.DailySavingsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(DailySavingsController.BASE_URL)
public class DailySavingsController {
  public static final String BASE_URL = "{ownerUsername}/{pbName}/today";
  private final DailySavingMapper dailySavingMapper;
  private final DailySavingsService dailySavingsService;

  @GetMapping
  public ResponseEntity<DailySavingDTO> dailySaving() {
    return null;
  }

  @PostMapping
  @Validated(OnCreate.class)
  public ResponseEntity<DailySavingDTO> createDailySaving(
      @PathVariable String ownerUsername,
      @PathVariable String pbName,
      @RequestBody @Valid DailySavingDTO dailySavingDto
  ) {
    DailySaving providedDailySaving = dailySavingMapper.toDailySaving(dailySavingDto);
    DailySaving createdDailySaving = dailySavingsService.createDailySaving(ownerUsername, pbName, providedDailySaving);
    return ResponseEntity.ok(dailySavingMapper.toDailySavingDto(createdDailySaving));
  }

  @PatchMapping
  public void partialUpdateDailySaving() {

  }

  @DeleteMapping
  public void deleteDailySaving() {

  }

}
