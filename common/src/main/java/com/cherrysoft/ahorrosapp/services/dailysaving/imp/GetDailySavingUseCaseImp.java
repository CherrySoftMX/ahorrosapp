package com.cherrysoft.ahorrosapp.services.dailysaving.imp;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.params.DailySavingParams;
import com.cherrysoft.ahorrosapp.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.services.dailysaving.DailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.GetDailySavingUseCase;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
public class GetDailySavingUseCaseImp extends DailySavingUseCase implements GetDailySavingUseCase {

  public GetDailySavingUseCaseImp(
      PiggyBankService pbService,
      DailySavingRepository dailySavingRepository
  ) {
    super(pbService, dailySavingRepository);
  }

  @Override
  public Optional<DailySaving> getDailySaving(DailySavingParams params) {
    setParams(params);
    PiggyBank pb = getCorrespondingPiggyBank();
    return dailySavingRepository.findByPiggyBankAndDate(pb, params.getDate());
  }

  @Override
  public DailySaving getDailySavingOrThrowIfNotPresent(DailySavingParams params) {
    return getDailySaving(params)
        .orElseThrow(() -> new RuntimeException(
            String.format("No saving for date: %s", params.getDate())
        ));
  }

}
