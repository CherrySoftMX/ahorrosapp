package com.cherrysoft.ahorrosapp.services.dailysaving;

import com.cherrysoft.ahorrosapp.models.DailySaving;
import com.cherrysoft.ahorrosapp.models.PiggyBank;
import com.cherrysoft.ahorrosapp.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.services.queries.DailySavingQueryParams;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetDailySavingUseCase extends DailySavingUseCase {

  public GetDailySavingUseCase(
      PiggyBankService pbService,
      DailySavingRepository dailySavingRepository
  ) {
    super(pbService, dailySavingRepository);
  }

  public Optional<DailySaving> getDailySaving(DailySavingQueryParams params) {
    setParams(params);
    PiggyBank pb = getCorrespondingPiggyBank();
    return dailySavingRepository.findByPiggyBankAndDate(pb, params.getDate());
  }

  public DailySaving getDailySavingOrThrowIfNotPresent(DailySavingQueryParams params) {
    setParams(params);
    return getDailySaving(getParams())
        .orElseThrow(this::createExceptionIfSavingNotPresent);
  }

  protected RuntimeException createExceptionIfSavingNotPresent() {
    return new RuntimeException("No daily saving!");
  }

}
