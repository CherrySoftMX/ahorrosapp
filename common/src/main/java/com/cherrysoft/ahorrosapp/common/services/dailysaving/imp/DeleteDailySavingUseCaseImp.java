package com.cherrysoft.ahorrosapp.common.services.dailysaving.imp;

import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.common.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.common.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.DailySavingUseCase;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.DeleteDailySavingUseCase;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.GetDailySavingUseCase;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
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
  public DailySaving deleteDailySaving(DailySavingSpec dailySavingSpec) {
    DailySaving dailySaving = getDailySavingUseCase.getDailySavingOrElseThrow(dailySavingSpec);
    dailySavingRepository.delete(dailySaving);
    return dailySaving;
  }

}
