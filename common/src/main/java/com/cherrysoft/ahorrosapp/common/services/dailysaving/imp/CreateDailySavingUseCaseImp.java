package com.cherrysoft.ahorrosapp.common.services.dailysaving.imp;

import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.common.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.CreateDailySavingUseCase;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.DailySavingUseCase;
import com.cherrysoft.ahorrosapp.common.utils.BeanUtils;
import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.GetDailySavingUseCase;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Primary
@Component
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
  public DailySaving createDailySaving(DailySavingSpec dailySavingSpec) {
    setDailySavingSpec(dailySavingSpec);
    ensureDailySavingDateIsWithinPbSavingsInterval();
    return createDailySaving();
  }

  private DailySaving createDailySaving() {
    Optional<DailySaving> dailySavingMaybe = getDailySavingUseCase.getDailySaving(getDailySavingSpec());
    return dailySavingMaybe
        .map(this::overrideSavedDailySaving)
        .orElseGet(this::addDailySavingToPiggyBank);
  }

  private DailySaving overrideSavedDailySaving(DailySaving savedDailySaving) {
    DailySaving payload = getDailySavingSpec().getDailySaving();
    BeanUtils.copyProperties(payload, savedDailySaving);
    return dailySavingRepository.save(savedDailySaving);
  }

  private DailySaving addDailySavingToPiggyBank() {
    PiggyBank pb = getCorrespondingPiggyBank();
    DailySaving payload = getDailySavingSpec().getDailySaving();
    pb.addDailySaving(payload);
    return dailySavingRepository.save(payload);
  }

}
