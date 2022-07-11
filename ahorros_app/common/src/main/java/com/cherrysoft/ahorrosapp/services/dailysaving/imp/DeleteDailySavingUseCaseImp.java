package com.cherrysoft.ahorrosapp.services.dailysaving.imp;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.queryparams.DailySavingQueryParams;
import com.cherrysoft.ahorrosapp.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.services.dailysaving.DailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.DeleteDailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.GetDailySavingUseCase;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class DeleteDailySavingUseCaseImp extends DailySavingUseCase implements DeleteDailySavingUseCase {
  private final GetDailySavingUseCase getDailySavingUseCase;

  public DeleteDailySavingUseCaseImp(
      PiggyBankService pbService,
      DailySavingRepository dailySavingRepository,
      GetDailySavingUseCase getDailySavingUseCase
  ) {
    super(pbService, dailySavingRepository);
    this.getDailySavingUseCase = getDailySavingUseCase;
  }

  @Override
  public DailySaving deleteDailySaving(DailySavingQueryParams params) {
    DailySaving dailySaving = getDailySavingUseCase.getDailySavingOrThrowIfNotPresent(params);
    dailySavingRepository.delete(dailySaving);
    return dailySaving;
  }

}
