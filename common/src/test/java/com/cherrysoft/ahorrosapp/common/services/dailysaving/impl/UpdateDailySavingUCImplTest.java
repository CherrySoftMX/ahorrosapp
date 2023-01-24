package com.cherrysoft.ahorrosapp.common.services.dailysaving.impl;

import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.core.models.SavingsDateRange;
import com.cherrysoft.ahorrosapp.common.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.common.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.common.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.UpdateDailySavingUC;
import com.cherrysoft.ahorrosapp.common.services.exceptions.saving.SavingNotFoundException;
import com.cherrysoft.ahorrosapp.common.services.exceptions.saving.SavingOutOfDateRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.cherrysoft.ahorrosapp.common.utils.DateUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateDailySavingUCImplTest {
  @Mock private PiggyBankService pbService;
  @Mock private DailySavingRepository dailySavingRepository;
  private UpdateDailySavingUC updateDailySavingUC;
  private PiggyBank pb;

  @BeforeEach
  void init() {
    var getDailySavingUC = new GetDailySavingUCImpl(pbService, dailySavingRepository);
    updateDailySavingUC = new UpdateDailySavingUCImpl(pbService, dailySavingRepository, getDailySavingUC);
    pb = PiggyBank.builder()
        .name("test-piggy")
        .savingsDateRange(new SavingsDateRange(today(), aWeekInTheFuture()))
        .build();
  }

  @Test
  void shouldPartiallyUpdateDailySaving() {
    DailySaving updatedDailySaving = DailySaving.builder()
        .amount(BigDecimal.valueOf(100))
        .build();
    DailySaving savedDailySaving = DailySaving.builder()
        .date(today())
        .amount(BigDecimal.TEN)
        .description("Sample description")
        .build();
    given(pbService.getPiggyBankByName("test-username", "test-piggy")).willReturn(pb);
    given(dailySavingRepository.findByPiggyBankAndDate(any(), any())).willReturn(Optional.of(savedDailySaving));
    given(dailySavingRepository.save(savedDailySaving)).willAnswer(invocation -> invocation.getArgument(0));

    DailySaving result = updateDailySavingUC.updateDailySaving(new DailySavingSpec("test-username", "test-piggy", updatedDailySaving));

    assertEquals(today(), result.getDate());
    assertEquals(BigDecimal.valueOf(100), result.getAmount());
    assertEquals("Sample description", result.getDescription());
  }

  @Test
  void shouldThrowException_whenDailySavingNotFound() {
    DailySaving updatedDailySaving = DailySaving.builder()
        .date(today())
        .amount(BigDecimal.TEN)
        .build();
    given(pbService.getPiggyBankByName("test-username", "test-piggy")).willReturn(pb);
    given(dailySavingRepository.findByPiggyBankAndDate(any(), any())).willReturn(Optional.empty());

    assertThrows(SavingNotFoundException.class, () -> {
      updateDailySavingUC.updateDailySaving(new DailySavingSpec("test-username", "test-piggy", updatedDailySaving));
    });
  }

  @Test
  void shouldThrowException_whenUpdatedDateIsBeforePiggyBankStartDate() {
    DailySaving updatedDailySaving = DailySaving.builder()
        .date(yesterday())
        .build();
    given(pbService.getPiggyBankByName("test-username", "test-piggy")).willReturn(pb);

    assertThrows(SavingOutOfDateRangeException.class, () -> {
      updateDailySavingUC.updateDailySaving(new DailySavingSpec("test-username", "test-piggy", updatedDailySaving));
    });
    verify(dailySavingRepository, never()).save(any());
  }

  @Test
  void shouldThrowException_whenUpdatedDateIsAfterPiggyBankEndDate() {
    DailySaving updatedDailySaving = DailySaving.builder()
        .date(aWeekInTheFuture().plusDays(1))
        .build();
    given(pbService.getPiggyBankByName("test-username", "test-piggy")).willReturn(pb);

    assertThrows(SavingOutOfDateRangeException.class, () -> {
      updateDailySavingUC.updateDailySaving(new DailySavingSpec("test-username", "test-piggy", updatedDailySaving));
    });
    verify(dailySavingRepository, never()).save(any());
  }

}