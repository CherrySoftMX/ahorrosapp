package com.cherrysoft.ahorrosapp.services.dailysaving.imp;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.queryparams.DailySavingQueryParams;
import com.cherrysoft.ahorrosapp.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.services.dailysaving.DailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.GetDailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.PartialUpdateDailySavingUseCase;
import com.cherrysoft.ahorrosapp.utils.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class PartialUpdateDailySavingUseCaseImp extends DailySavingUseCase implements PartialUpdateDailySavingUseCase {
  private final GetDailySavingUseCase getDailySavingUseCase;

  public PartialUpdateDailySavingUseCaseImp(
      PiggyBankService pbService,
      DailySavingRepository dailySavingRepository,
      GetDailySavingUseCase getDailySavingUseCase
  ) {
    super(pbService, dailySavingRepository);
    this.getDailySavingUseCase = getDailySavingUseCase;
  }

  @Override
  public DailySaving partialUpdateDailySaving(
      DailySavingQueryParams params,
      DailySaving updatedDailySaving
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
