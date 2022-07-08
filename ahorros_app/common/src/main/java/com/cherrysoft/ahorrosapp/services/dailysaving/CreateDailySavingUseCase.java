package com.cherrysoft.ahorrosapp.services.dailysaving;

import com.cherrysoft.ahorrosapp.models.DailySaving;
import com.cherrysoft.ahorrosapp.models.PiggyBank;
import com.cherrysoft.ahorrosapp.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.services.queries.DailySavingQueryParams;
import com.cherrysoft.ahorrosapp.utils.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CreateDailySavingUseCase extends DailySavingUseCase {
  private final GetDailySavingUseCase getDailySavingUseCase;

  public CreateDailySavingUseCase(
      PiggyBankService pbService,
      GetDailySavingUseCase getDailySavingUseCase,
      DailySavingRepository dailySavingRepository
  ) {
    super(pbService, dailySavingRepository);
    this.getDailySavingUseCase = getDailySavingUseCase;
  }

  public DailySaving createDailySaving(
      DailySavingQueryParams params, DailySaving dailySaving
  ) {
    setParams(params);
    setDailySaving(dailySaving);
    ensureDailySavingDateIsWithinPbSavingsInterval();
    return createDailySaving();
  }

  private DailySaving createDailySaving() {
    Optional<DailySaving> dailySavingMaybe = getDailySavingUseCase.getDailySaving(params);
    return dailySavingMaybe
        .map(this::overrideSavedDailySaving)
        .orElseGet(this::addDailySavingToPiggyBank);
  }

  private DailySaving overrideSavedDailySaving(DailySaving savedDailySaving) {
    BeanUtils.copyProperties(dailySaving, savedDailySaving);
    return dailySavingRepository.save(savedDailySaving);
  }

  private DailySaving addDailySavingToPiggyBank() {
    dailySaving.setDate(params.getDate());
    PiggyBank pb = getCorrespondingPiggyBank();
    pb.addDailySaving(dailySaving);
    return dailySavingRepository.save(dailySaving);
  }

}
