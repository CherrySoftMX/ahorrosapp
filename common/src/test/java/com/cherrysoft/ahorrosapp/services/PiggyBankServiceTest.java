package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.models.User;
import com.cherrysoft.ahorrosapp.repositories.PiggyBankRepository;
import com.cherrysoft.ahorrosapp.services.exceptions.piggybank.InvalidSavingsIntervalException;
import com.cherrysoft.ahorrosapp.services.exceptions.piggybank.PiggyBankNameNotAvailableException;
import com.cherrysoft.ahorrosapp.services.exceptions.piggybank.PiggyBankNotFoundException;
import com.cherrysoft.ahorrosapp.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.cherrysoft.ahorrosapp.utils.DateUtils.today;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PiggyBankServiceTest {
  @Mock private PiggyBankRepository pbRepository;
  @Mock private UserService userService;
  @InjectMocks private PiggyBankService pbService;

  @Test
  void whenGivenAnExistentPbNameAndOwner_thenReturnsCorrespondingPb() {
    PiggyBank pb = TestUtils.PiggyBanks.newPiggyBankWithOwner();
    given(pbRepository.findByNameAndOwner(pb.getName(), pb.getOwner())).willReturn(Optional.of(pb));

    pb = pbService.getPiggyBankByName(pb.getOwner(), pb.getName());

    assertNotNull(pb);
  }

  @Test
  void whenPbNameOrOwnerDoesNotExists_thenThrowsAnException() {
    PiggyBank pb = TestUtils.PiggyBanks.newPiggyBankWithOwner();
    given(pbRepository.findByNameAndOwner(any(), any())).willReturn(Optional.empty());

    assertThrows(PiggyBankNotFoundException.class, () -> {
      pbService.getPiggyBankByName(pb.getOwner(), pb.getName());
    });
  }

  @Test
  void whenPbAddedToUser_thenReturnsPbWithUserAsOwner_andUserHasNewPb() {
    User owner = TestUtils.Users.newUser();
    PiggyBank newPb = TestUtils.PiggyBanks.newPiggyBank();
    given(userService.getUserByUsername(owner.getUsername())).willReturn(owner);
    given(pbRepository.existsByNameAndOwner(any(), any())).willReturn(false);
    given(pbRepository.findByNameAndOwner(newPb.getName(), owner)).willReturn(Optional.of(newPb));

    newPb = pbService.addPiggyBankTo(owner.getUsername(), newPb);

    assertEquals(owner, newPb.getOwner());
    assertThat(owner.getPiggyBanks(), hasSize(1));
  }

  @Test
  void givenAddPbToUser_whenPbHasNotInitialStartDate_thenInitialStartIsSetToday() {
    User owner = TestUtils.Users.newUser();
    PiggyBank newPb = TestUtils.PiggyBanks.newPiggyBankNoStartDate();
    given(userService.getUserByUsername(owner.getUsername())).willReturn(owner);
    given(pbRepository.existsByNameAndOwner(any(), any())).willReturn(false);
    given(pbRepository.findByNameAndOwner(newPb.getName(), owner)).willReturn(Optional.of(newPb));

    newPb = pbService.addPiggyBankTo(owner.getUsername(), newPb);

    assertTrue(newPb.hasStartSavingsDate());
    assertEquals(newPb.getStartSavings(), today());
  }

  @Test
  void givenInvalidSavingsInterval_whenStartSavingsIsAfterEndSavings_thenThrowsAnExceptions() {
    User owner = TestUtils.Users.newUser();
    PiggyBank newPb = TestUtils.PiggyBanks.newPiggyBankNoStartDate();
    newPb.setStartSavings(today().plusDays(14));
    given(userService.getUserByUsername(owner.getUsername())).willReturn(owner);
    given(pbRepository.existsByNameAndOwner(any(), any())).willReturn(false);

    assertThrows(InvalidSavingsIntervalException.class, () -> {
      pbService.addPiggyBankTo(owner.getUsername(), newPb);
    });
  }

  @Test
  void givenInvalidSavingsInterval_whenStartSavingsIsEqualToEndSavings_thenThrowsAnException() {
    User owner = TestUtils.Users.newUser();
    PiggyBank newPb = TestUtils.PiggyBanks.newPiggyBankNoStartDate();
    newPb.setStartSavings(newPb.getEndSavings());
    given(userService.getUserByUsername(owner.getUsername())).willReturn(owner);
    given(pbRepository.existsByNameAndOwner(any(), any())).willReturn(false);

    assertThrows(InvalidSavingsIntervalException.class, () -> {
      pbService.addPiggyBankTo(owner.getUsername(), newPb);
    });
  }

  @Test
  void whenPbNameAlreadyTakenForUser_thenThrowsAnException() {
    User owner = TestUtils.Users.newUser();
    PiggyBank newPb = TestUtils.PiggyBanks.newPiggyBank();
    given(pbRepository.existsByNameAndOwner(any(), any())).willReturn(true);
    given(userService.getUserByUsername(owner.getUsername())).willReturn(owner);

    assertThrows(PiggyBankNameNotAvailableException.class, () -> {
      pbService.addPiggyBankTo(owner.getUsername(), newPb);
    });
  }

  @Test
  void whenDeletePiggyBank_thenPbGetsRemovedFromOwner() {
    PiggyBank pb = TestUtils.PiggyBanks.newPiggyBankWithOwner();
    User owner = pb.getOwner();
    given(userService.getUserByUsername(owner.getUsername())).willReturn(owner);
    given(pbRepository.findByNameAndOwner(pb.getName(), owner)).willReturn(Optional.of(pb));

    pb = pbService.deletePiggyBank(owner.getUsername(), pb.getName());

    assertThat(owner.getPiggyBanks(), not(hasItem(pb)));
  }

}
