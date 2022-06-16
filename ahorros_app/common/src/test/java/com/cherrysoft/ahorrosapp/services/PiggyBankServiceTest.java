package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.models.PiggyBank;
import com.cherrysoft.ahorrosapp.models.User;
import com.cherrysoft.ahorrosapp.repositories.PiggyBankRepository;
import com.cherrysoft.ahorrosapp.services.exceptions.piggybank.PiggyBankNameNotAvailableException;
import com.cherrysoft.ahorrosapp.services.exceptions.piggybank.PiggyBankNotFoundException;
import com.cherrysoft.ahorrosapp.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.cherrysoft.ahorrosapp.utils.DateUtils.today;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PiggyBankServiceTest {
  @Mock private PiggyBankRepository pbRepository;
  @Mock private UserService userService;
  @InjectMocks private PiggyBankService pbService;
  private PiggyBank dummyPb;

  @BeforeEach
  void setUp() {
    User owner = TestUtils.Users.newUser();
    dummyPb = TestUtils.PiggyBanks.newPiggyBank();
    dummyPb.setOwner(owner);
  }

  @Test
  void whenGivenAnExistentPbNameAndOwner_thenReturnsCorrespondingPb() {
    given(pbRepository.findByNameAndOwner(any(), any())).willReturn(Optional.of(dummyPb));

    PiggyBank pb = pbService.getPiggyBankByName(dummyPb.getName(), dummyPb.getOwner());

    assertNotNull(pb);
  }

  @Test
  void whenPbNameOrOwnerDoesNotExists_thenThrowsAnException() {
    given(pbRepository.findByNameAndOwner(any(), any())).willReturn(Optional.empty());

    assertThrows(PiggyBankNotFoundException.class, () -> {
      pbService.getPiggyBankByName(dummyPb.getName(), dummyPb.getOwner());
    });
  }

  @Test
  void whenPbAddedToUser_thenReturnsPbWithUserAsOwner_andUserHasNewPb() {
    User owner = TestUtils.Users.newUser();
    PiggyBank newPb = TestUtils.PiggyBanks.newPiggyBank();
    fixtureAddPbToUser(owner, newPb);

    newPb = pbService.addPiggyBankTo(owner.getUsername(), newPb);

    assertEquals(owner, newPb.getOwner());
    assertThat(owner.getPiggyBanks(), hasSize(1));
  }

  @Test
  void givenAddPbToUser_whenPbHasNotInitialStartDate_thenInitialStartIsSetToday() {
    User owner = TestUtils.Users.newUser();
    PiggyBank newPb = TestUtils.PiggyBanks.newPiggyBankNoStartDate();
    fixtureAddPbToUser(owner, newPb);

    newPb = pbService.addPiggyBankTo(owner.getUsername(), newPb);

    assertTrue(newPb.hasStartSavingsDate());
    assertEquals(newPb.getSavingInterval().getStartDate(), today());
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

  private void fixtureAddPbToUser(User owner, PiggyBank newPb) {
    given(userService.getUserByUsername(owner.getUsername())).willReturn(owner);
    given(pbRepository.existsByNameAndOwner(any(), any())).willReturn(false);
    given(pbRepository.findByNameAndOwner(newPb.getName(), owner)).willReturn(Optional.of(newPb));
  }

}