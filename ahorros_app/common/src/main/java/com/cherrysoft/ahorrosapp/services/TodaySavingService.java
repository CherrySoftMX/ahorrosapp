package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.models.DailySaving;
import com.cherrysoft.ahorrosapp.models.PiggyBank;
import com.cherrysoft.ahorrosapp.repositories.TodaySavingRepository;
import com.cherrysoft.ahorrosapp.utils.BeanUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.cherrysoft.ahorrosapp.utils.DateUtils.today;

@Service
@AllArgsConstructor
public class TodaySavingService {
  private final PiggyBankService pbService;
  private final TodaySavingRepository todaySavingRepository;

  public Optional<DailySaving> getTodaySaving(String ownerUsername, String pbName) {
    PiggyBank pb = pbService.getPiggyBankByName(pbName, ownerUsername);
    return todaySavingRepository.findByPiggyBankAndDate(pb, today());
  }

  public DailySaving getTodaySavingOrThrowIfNotPresent(String ownerUsername, String pbName) {
    return getTodaySaving(ownerUsername, pbName)
        .orElseThrow(() -> new RuntimeException("No today saving!"));
  }

  public DailySaving createTodaySaving(
      String ownerUsername, String pbName, DailySaving todaySaving
  ) {
    Optional<DailySaving> todaySavingMaybe = getTodaySaving(ownerUsername, pbName);
    if (todaySavingMaybe.isPresent()) {
      DailySaving savedTodaySaving = todaySavingMaybe.get();
      BeanUtils.copyProperties(todaySaving, savedTodaySaving);
      return todaySavingRepository.save(savedTodaySaving);
    } else {
      todaySaving.setDate(today());
      PiggyBank pb = pbService.getPiggyBankByName(pbName, ownerUsername);
      pb.addDailySaving(todaySaving);
      return todaySavingRepository.save(todaySaving);
    }
  }

  public DailySaving partialUpdateTodaySaving(
      String ownerUsername, String pbName, DailySaving updatedTodaySaving
  ) {
    DailySaving todaySaving = getTodaySavingOrThrowIfNotPresent(ownerUsername, pbName);
    BeanUtils.copyProperties(updatedTodaySaving, todaySaving);
    return todaySavingRepository.save(todaySaving);
  }

  public DailySaving deleteTodaySaving(String ownerUsername, String pbName) {
    DailySaving todaySaving = getTodaySavingOrThrowIfNotPresent(ownerUsername, pbName);
    todaySavingRepository.delete(todaySaving);
    return todaySaving;
  }

}
