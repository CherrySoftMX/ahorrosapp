package com.cherrysoft.ahorrosapp.services.dailysaving.imp;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.queryparams.DailySavingQueryParams;
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

  public GetDailySavingUseCaseImp(PiggyBankService pbService, DailySavingRepository dsRepo) {
    super(pbService, dsRepo);
  }

  @Override
  public Optional<DailySaving> getDailySaving(DailySavingQueryParams params) {
    setParams(params);
    PiggyBank pb = getCorrespondingPiggyBank();
    return dailySavingRepository.findByPiggyBankAndDate(pb, params.getDate());
  }

  @Override
  public DailySaving getDailySavingOrThrowIfNotPresent(DailySavingQueryParams params) {
    setParams(params);
    return getDailySaving(getParams())
        .orElseThrow(() -> new RuntimeException(
            String.format("No saving for date: %s", params.getDate())
        ));
  }

}
