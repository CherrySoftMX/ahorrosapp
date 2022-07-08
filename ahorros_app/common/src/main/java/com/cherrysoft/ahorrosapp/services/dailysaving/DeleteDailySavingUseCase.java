package com.cherrysoft.ahorrosapp.services.dailysaving;

import com.cherrysoft.ahorrosapp.models.DailySaving;
import com.cherrysoft.ahorrosapp.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.services.queries.DailySavingQueryParams;
import org.springframework.stereotype.Component;

@Component
public class DeleteDailySavingUseCase extends DailySavingUseCase {
  private final GetDailySavingUseCase getDailySavingUseCase;

  public DeleteDailySavingUseCase(
      PiggyBankService pbService,
      DailySavingRepository dailySavingRepository,
      GetDailySavingUseCase getDailySavingUseCase
  ) {
    super(pbService, dailySavingRepository);
    this.getDailySavingUseCase = getDailySavingUseCase;
  }

  public DailySaving deleteDailySaving(DailySavingQueryParams params) {
    DailySaving dailySaving = getDailySavingUseCase.getDailySavingOrThrowIfNotPresent(params);
    dailySavingRepository.delete(dailySaving);
    return dailySaving;
  }

}
