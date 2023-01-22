package com.cherrysoft.ahorrosapp.common.services;

import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.core.models.SavingsDateRange;
import com.cherrysoft.ahorrosapp.common.core.models.User;
import com.cherrysoft.ahorrosapp.common.core.models.specs.piggybank.UpdatePiggyBankSpec;
import com.cherrysoft.ahorrosapp.common.repositories.PiggyBankRepository;
import com.cherrysoft.ahorrosapp.common.services.exceptions.piggybank.InvalidSavingsIntervalException;
import com.cherrysoft.ahorrosapp.common.services.exceptions.piggybank.PiggyBankNameNotAvailableException;
import com.cherrysoft.ahorrosapp.common.services.exceptions.piggybank.PiggyBankNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.cherrysoft.ahorrosapp.common.utils.DateUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PiggyBankServiceTest {
  @Mock private PiggyBankRepository pbRepository;
  @Mock private UserService userService;
  @InjectMocks private PiggyBankService pbService;
  private User owner;

  @BeforeEach
  void init() {
    owner = User.builder().username("test-username").build();
  }

  @Nested
  @DisplayName("Get a piggy bank")
  class GetPiggyBank {

    @Test
    void shouldThrowException_whenPiggyBankNotFound() {
      User owner = User.builder().username("test-username").build();
      given(pbRepository.findByNameAndOwner(anyString(), any())).willReturn(Optional.empty());

      assertThrows(PiggyBankNotFoundException.class, () -> {
        pbService.getPiggyBankByName(owner, "invalid-piggy");
      });
    }

  }

  @Nested
  @DisplayName("Add a piggy bank to user")
  class AddPiggyBank {

    @Test
    void shouldCreatePiggyBank_andGetsAddedToOwner() {
      PiggyBank newPb = PiggyBank.builder().name("test-piggy").build();
      given(userService.getUserByUsername(owner.getUsername())).willReturn(owner);

      pbService.addPiggyBankTo("test-username", newPb);

      assertEquals(1, owner.getPiggyBanks().size());
    }

    @Test
    void shouldSetDateToToday_whenNotProvided() {
      PiggyBank newPb = PiggyBank.builder().name("test-piggy").build();
      newPb.setEndSavings(aWeekInTheFuture());
      given(userService.getUserByUsername(owner.getUsername())).willReturn(owner);
      given(pbRepository.saveAndFlush(newPb)).willAnswer(invocation -> invocation.getArgument(0));

      pbService.addPiggyBankTo("test-username", newPb);

      assertEquals(today(), newPb.getStartSavings());
    }

    @Test
    void shouldThrowException_whenEndDateIsBeforeStartDate() {
      PiggyBank newPb = PiggyBank.builder()
          .name("test-piggy")
          .savingsDateRange(new SavingsDateRange(today(), yesterday()))
          .build();
      given(userService.getUserByUsername(owner.getUsername())).willReturn(owner);

      assertThrows(InvalidSavingsIntervalException.class, () -> {
        pbService.addPiggyBankTo("test-username", newPb);
      }, "Start date must be before the end date");
    }

    @Test
    void shouldThrowException_whenNoStartDateAndEndDateIsPast() {
      PiggyBank newPb = PiggyBank.builder().name("test-piggy").build();
      newPb.setEndSavings(yesterday());
      given(userService.getUserByUsername(owner.getUsername())).willReturn(owner);

      assertThrows(InvalidSavingsIntervalException.class, () -> {
        pbService.addPiggyBankTo("test-username", newPb);
      }, "End date must be present of future");
    }

    @Test
    void shouldThrowException_whenPiggyBankNameIsNotUnique() {
      PiggyBank newPb = PiggyBank.builder().name("invalid-piggy").build();
      given(pbRepository.existsByNameAndOwner(anyString(), any())).willReturn(true);
      given(userService.getUserByUsername(owner.getUsername())).willReturn(owner);

      assertThrows(PiggyBankNameNotAvailableException.class, () -> {
        pbService.addPiggyBankTo("test-username", newPb);
      });
    }

  }

  @Nested
  @DisplayName("Update a piggy bank")
  class UpdatePiggyBank {

    @Test
    void shouldPartiallyUpdatePiggyBank() {
      PiggyBank providedPb = PiggyBank.builder()
          .name("new-piggy")
          .initialAmount(BigDecimal.valueOf(100))
          .savingsDateRange(new SavingsDateRange(null, aWeekInTheFuture()))
          .build();
      PiggyBank savedPb = PiggyBank.builder()
          .name("old-piggy")
          .borrowedAmount(BigDecimal.valueOf(500))
          .savingsDateRange(new SavingsDateRange(today(), tomorrow()))
          .build();
      given(userService.getUserByUsername(owner.getUsername())).willReturn(owner);
      given(pbRepository.findByNameAndOwner(anyString(), any())).willReturn(Optional.of(savedPb));
      given(pbRepository.save(savedPb)).willAnswer(invocation -> invocation.getArgument(0));

      PiggyBank result = pbService.partialUpdatePiggyBank(new UpdatePiggyBankSpec("test-username", "test-piggy", providedPb));

      assertEquals("new-piggy", result.getName());
      assertEquals(BigDecimal.valueOf(100), result.getInitialAmount());
      assertEquals(BigDecimal.valueOf(500), result.getBorrowedAmount());
      assertEquals(today(), result.getStartSavings());
      assertEquals(aWeekInTheFuture(), result.getEndSavings());
    }

    @Test
    void shouldThrowException_whenPiggyBankNameAlreadyTaken() {
      PiggyBank providedPb = PiggyBank.builder().name("new-piggy").build();
      PiggyBank savedPb = PiggyBank.builder().name("old-piggy").build();
      given(userService.getUserByUsername(owner.getUsername())).willReturn(owner);
      given(pbRepository.findByNameAndOwner(anyString(), any())).willReturn(Optional.of(savedPb));
      given(pbRepository.existsByNameAndOwner(anyString(), any())).willReturn(true);

      assertThrows(PiggyBankNameNotAvailableException.class, () -> {
        pbService.partialUpdatePiggyBank(new UpdatePiggyBankSpec("test-username", "old-piggy", providedPb));
      });
    }

    @Test
    void shouldThrowException_whenUpdatedStartDateIfAfterEndDate() {
      PiggyBank providedPb = PiggyBank.builder()
          .savingsDateRange(new SavingsDateRange(aWeekInTheFuture(), null))
          .build();
      PiggyBank savedPb = PiggyBank.builder()
          .savingsDateRange(new SavingsDateRange(today(), tomorrow()))
          .build();
      given(userService.getUserByUsername(owner.getUsername())).willReturn(owner);
      given(pbRepository.findByNameAndOwner(anyString(), any())).willReturn(Optional.of(savedPb));

      assertThrows(InvalidSavingsIntervalException.class, () -> {
        pbService.partialUpdatePiggyBank(new UpdatePiggyBankSpec("test-username", "test-piggy", providedPb));
      });
    }

    @Test
    void shouldThrowException_whenUpdatedEndDateIsBeforeStartDate() {
      PiggyBank providedPb = PiggyBank.builder()
          .savingsDateRange(new SavingsDateRange(null, today()))
          .build();
      PiggyBank savedPb = PiggyBank.builder()
          .savingsDateRange(new SavingsDateRange(tomorrow(), null))
          .build();
      given(userService.getUserByUsername(owner.getUsername())).willReturn(owner);
      given(pbRepository.findByNameAndOwner(anyString(), any())).willReturn(Optional.of(savedPb));

      assertThrows(InvalidSavingsIntervalException.class, () -> {
        pbService.partialUpdatePiggyBank(new UpdatePiggyBankSpec("test-username", "test-piggy", providedPb));
      });
    }

  }

  @Nested
  @DisplayName("Delete a piggy bank")
  class DeletePiggyBank {

    @Test
    void shouldRemovePiggyBankFromOwner() {
      PiggyBank pb = PiggyBank.builder().name("test-piggy").build();
      owner.addPiggyBank(pb);
      given(userService.getUserByUsername(owner.getUsername())).willReturn(owner);
      given(pbRepository.findByNameAndOwner(anyString(), any())).willReturn(Optional.of(pb));

      pbService.deletePiggyBank("test-username", "test-piggy");

      assertThat(owner.getPiggyBanks(), is(empty()));
    }

    @Test
    void shouldThrowException_whenPiggyBankNotFound() {
      given(pbRepository.findByNameAndOwner(anyString(), any())).willReturn(Optional.empty());

      assertThrows(PiggyBankNotFoundException.class, () -> {
        pbService.deletePiggyBank("test-username", "invalid-piggy");
      });
    }
  }

}
