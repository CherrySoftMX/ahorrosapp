package com.cherrysoft.ahorrosapp.common.services.dailysaving.impl;

import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.core.models.SavingsDateRange;
import com.cherrysoft.ahorrosapp.common.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.common.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.common.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.DeleteDailySavingUC;
import com.cherrysoft.ahorrosapp.common.services.exceptions.saving.SavingNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.cherrysoft.ahorrosapp.common.utils.DateUtils.aWeekInTheFuture;
import static com.cherrysoft.ahorrosapp.common.utils.DateUtils.today;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DeleteDailySavingUCImplTest {
  @Mock private PiggyBankService pbService;
  @Mock private DailySavingRepository dailySavingRepository;
  private DeleteDailySavingUC deleteDailySavingUC;

  @BeforeEach
  void init() {
    var getDailySavingUC = new GetDailySavingUCImpl(pbService, dailySavingRepository);
    deleteDailySavingUC = new DeleteDailySavingUCImpl(pbService, dailySavingRepository, getDailySavingUC);
  }

  @Test
  void shouldDeleteDailySavingFromPiggyBank() {
    PiggyBank pb = PiggyBank.builder()
        .name("test-piggy")
        .savingsDateRange(new SavingsDateRange(today(), aWeekInTheFuture()))
        .build();
    DailySaving savingToDelete = DailySaving.builder()
        .date(today())
        .build();
    pb.addDailySaving(savingToDelete);
    given(pbService.getPiggyBankByName("test-username", "test-piggy")).willReturn(pb);
    given(dailySavingRepository.findByPiggyBankAndDate(any(), any())).willReturn(Optional.of(savingToDelete));

    deleteDailySavingUC.deleteDailySaving(new DailySavingSpec("test-username", "test-piggy", today()));

    assertThat(pb.getDailySavings(), hasSize(0));
  }

  @Test
  void shouldThrowException_whenDailySavingNotFound() {
    given(dailySavingRepository.findByPiggyBankAndDate(any(), any())).willReturn(Optional.empty());

    assertThrows(SavingNotFoundException.class, () -> {
      deleteDailySavingUC.deleteDailySaving(new DailySavingSpec("test-username", "test-piggy", today()));
    });
  }

}