package com.cherrysoft.ahorrosapp.common.services.dailysaving.imp;

import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.common.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.common.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.DailySavingUseCase;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.GetDailySavingUseCase;
import com.cherrysoft.ahorrosapp.common.services.exceptions.saving.SavingNotFoundException;
import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Primary
@Component
public class GetDailySavingUseCaseImp extends DailySavingUseCase implements GetDailySavingUseCase {

  public GetDailySavingUseCaseImp(
      PiggyBankService pbService,
      DailySavingRepository dailySavingRepository
  ) {
    super(pbService, dailySavingRepository);
  }

  @Override
  public Optional<DailySaving> getDailySaving(DailySavingSpec dailySavingSpec) {
    setDailySavingSpec(dailySavingSpec);
    PiggyBank pb = getCorrespondingPiggyBank();
    return dailySavingRepository.findByPiggyBankAndDate(pb, dailySavingSpec.getSavingDate());
  }

  @Override
  public DailySaving getDailySavingOrElseThrow(DailySavingSpec dailySavingSpec) {
    return getDailySaving(dailySavingSpec)
        .orElseThrow(() -> new SavingNotFoundException(dailySavingSpec.getSavingDate()));
  }

}
