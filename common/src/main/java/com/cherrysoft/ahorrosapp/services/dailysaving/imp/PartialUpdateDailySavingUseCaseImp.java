package com.cherrysoft.ahorrosapp.services.dailysaving.imp;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.services.dailysaving.DailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.GetDailySavingUseCase;
import com.cherrysoft.ahorrosapp.services.dailysaving.PartialUpdateDailySavingUseCase;
import com.cherrysoft.ahorrosapp.utils.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
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
  public DailySaving updateDailySaving(DailySavingSpec dailySavingSpec) {
    setDailySavingSpec(dailySavingSpec);
    ensureDailySavingDateIsWithinPbSavingsInterval();
    return partialUpdateDailySaving();
  }

  private DailySaving partialUpdateDailySaving() {
    DailySaving savedDailySaving = getDailySavingUseCase.getDailySavingOrElseThrow(getDailySavingSpec());
    DailySaving payload = getDailySavingSpec().getDailySaving();
    BeanUtils.copyProperties(payload, savedDailySaving);
    return dailySavingRepository.save(savedDailySaving);
  }

}
