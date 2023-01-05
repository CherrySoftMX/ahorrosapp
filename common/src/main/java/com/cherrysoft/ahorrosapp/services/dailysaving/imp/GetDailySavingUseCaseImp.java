package com.cherrysoft.ahorrosapp.services.dailysaving.imp;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.services.dailysaving.DailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.GetDailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.exceptions.saving.SavingNotFoundException;
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
