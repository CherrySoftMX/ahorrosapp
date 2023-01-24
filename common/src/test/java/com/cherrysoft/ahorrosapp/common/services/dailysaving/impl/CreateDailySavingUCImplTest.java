package com.cherrysoft.ahorrosapp.common.services.dailysaving.impl;

import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.core.models.SavingsDateRange;
import com.cherrysoft.ahorrosapp.common.core.models.specs.DailySavingSpec;
import com.cherrysoft.ahorrosapp.common.repositories.DailySavingRepository;
import com.cherrysoft.ahorrosapp.common.services.PiggyBankService;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.CreateDailySavingUC;
import com.cherrysoft.ahorrosapp.common.services.exceptions.saving.SavingOutOfDateRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.cherrysoft.ahorrosapp.common.utils.DateUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CreateDailySavingUCImplTest {
  @Mock private PiggyBankService pbService;
  @Mock private DailySavingRepository dailySavingRepository;
  private CreateDailySavingUC createDailySavingUC;
  private PiggyBank pb;

  @BeforeEach
  void init() {
    var getDailySavingUC = new GetDailySavingUCImpl(pbService, dailySavingRepository);
    createDailySavingUC = new CreateDailySavingUCImpl(pbService, dailySavingRepository, getDailySavingUC);
    pb = PiggyBank.builder()
        .name("test-piggy")
        .savingsDateRange(new SavingsDateRange(today(), aWeekInTheFuture()))
        .build();
  }

  @Test
  void shouldAddDailySavingToPiggyBank() {
    DailySaving dailySaving = DailySaving.builder().date(tomorrow()).build();
    given(pbService.getPiggyBankByName("test-username", "test-piggy")).willReturn(pb);
    given(dailySavingRepository.findByPiggyBankAndDate(any(), any())).willReturn(Optional.empty());

    createDailySavingUC.createDailySaving(new DailySavingSpec("test-username", "test-piggy", dailySaving));

    assertThat(pb.getDailySavings(), hasSize(1));
  }

  @Test
  void shouldOverrideDailySaving_whenAlreadyExistsForThatDay() {
    DailySaving savedDailySaving = DailySaving.builder()
        .date(today())
        .amount(BigDecimal.TEN)
        .description("Old description")
        .build();
    DailySaving newDailySaving = DailySaving.builder()
        .date(today())
        .amount(BigDecimal.valueOf(100))
        .description("New description")
        .build();
    pb.addDailySaving(savedDailySaving);
    given(pbService.getPiggyBankByName("test-username", "test-piggy")).willReturn(pb);
    given(dailySavingRepository.findByPiggyBankAndDate(any(), any())).willReturn(Optional.of(savedDailySaving));
    given(dailySavingRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

    DailySaving result = createDailySavingUC.createDailySaving(new DailySavingSpec("test-username", "test-piggy", newDailySaving));

    assertThat(pb.getDailySavings(), hasSize(1));
    assertEquals(today(), result.getDate());
    assertEquals(BigDecimal.valueOf(100), result.getAmount());
    assertEquals("New description", result.getDescription());
  }

  @Test
  void shouldThrowException_whenDailySavingDateIsAfterPiggyBankEndDate() {
    DailySaving newDailySaving = DailySaving.builder()
        .date(aWeekInTheFuture().plusDays(1))
        .build();
    given(pbService.getPiggyBankByName(anyString(), anyString())).willReturn(pb);

    assertThrows(SavingOutOfDateRangeException.class, () -> {
      createDailySavingUC.createDailySaving(new DailySavingSpec("test-username", "test-piggy", newDailySaving));
    });
  }

  @Test
  void shouldThrowException_whenDailySavingDateIsBeforePiggyBankStartDate() {
    DailySaving newDailySaving = DailySaving.builder()
        .date(yesterday())
        .build();
    given(pbService.getPiggyBankByName(anyString(), anyString())).willReturn(pb);

    assertThrows(SavingOutOfDateRangeException.class, () -> {
      createDailySavingUC.createDailySaving(new DailySavingSpec("test-username", "test-piggy", newDailySaving));
    });
  }

}