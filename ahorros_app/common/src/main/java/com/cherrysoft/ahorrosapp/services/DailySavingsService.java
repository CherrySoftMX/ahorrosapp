package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.models.DailySaving;
import com.cherrysoft.ahorrosapp.models.PiggyBank;
import com.cherrysoft.ahorrosapp.repositories.DailySavingsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.cherrysoft.ahorrosapp.utils.DateUtils.today;

@Service
@AllArgsConstructor
public class DailySavingsService {
  private final PiggyBankService pbService;
  private final DailySavingsRepository dailySavingsRepository;

  public DailySaving createDailySaving(
      String ownerUsername, String pbName, DailySaving dailySaving
  ) {
    dailySaving.setDate(today());
    PiggyBank pb = pbService.getPiggyBankByName(pbName, ownerUsername);
    pb.addDailySaving(dailySaving);
    return dailySavingsRepository.save(dailySaving);
  }

}
