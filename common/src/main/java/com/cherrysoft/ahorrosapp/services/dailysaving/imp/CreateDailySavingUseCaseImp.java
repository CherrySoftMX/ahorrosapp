package com.cherrysoft.ahorrosapp.services.dailysaving.imp;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.queryparams.DailySavingQueryParams;
import com.cherrysoft.ahorrosapp.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.services.dailysaving.CreateDailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.DailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.GetDailySavingUseCase;
import com.cherrysoft.ahorrosapp.utils.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
public class CreateDailySavingUseCaseImp extends DailySavingUseCase implements CreateDailySavingUseCase {
  private final GetDailySavingUseCase getDailySavingUseCase;

  public CreateDailySavingUseCaseImp(
      PiggyBankService pbService,
      DailySavingRepository dailySavingRepository,
      GetDailySavingUseCase getDailySavingUseCase
  ) {
    super(pbService, dailySavingRepository);
    this.getDailySavingUseCase = getDailySavingUseCase;
  }

  @Override
  public DailySaving createDailySaving(
      DailySavingQueryParams params,
      DailySaving dailySaving
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
