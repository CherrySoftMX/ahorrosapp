package com.cherrysoft.ahorrosapp.services.dailysaving;

import com.cherrysoft.ahorrosapp.models.DailySaving;
import com.cherrysoft.ahorrosapp.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.services.queries.DailySavingQueryParams;
import com.cherrysoft.ahorrosapp.utils.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class PartialUpdateDailySavingUseCase extends DailySavingUseCase {
  private final GetDailySavingUseCase getDailySavingUseCase;

  public PartialUpdateDailySavingUseCase(
      PiggyBankService pbService,
      DailySavingRepository dailySavingRepository,
      GetDailySavingUseCase getDailySavingUseCase
  ) {
    super(pbService, dailySavingRepository);
    this.getDailySavingUseCase = getDailySavingUseCase;
  }

  public DailySaving partialUpdateTodaySaving(
      DailySavingQueryParams params, DailySaving updatedDailySaving
  ) {
    setParams(params);
    setDailySaving(updatedDailySaving);
    ensureDailySavingDateIsWithinPbSavingsInterval();
    return partialUpdateDailySaving();
  }

  private DailySaving partialUpdateDailySaving() {
    DailySaving savedDailySaving = getDailySavingUseCase.getDailySavingOrThrowIfNotPresent(params);
    BeanUtils.copyProperties(dailySaving, savedDailySaving);
    return dailySavingRepository.save(savedDailySaving);
  }

}
