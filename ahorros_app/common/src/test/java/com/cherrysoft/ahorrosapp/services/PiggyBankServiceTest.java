package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.models.PiggyBank;
import com.cherrysoft.ahorrosapp.models.User;
import com.cherrysoft.ahorrosapp.repositories.PiggyBankRepository;
import com.cherrysoft.ahorrosapp.services.exceptions.piggybank.PiggyBankNotFoundException;
import com.cherrysoft.ahorrosapp.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
  void whenGivenAnExistentPiggyBankNameAndOwner_thenReturnsCorrespondingPiggyBank() {
    given(pbRepository.findByNameAndOwner(any(), any())).willReturn(Optional.of(dummyPb));

    PiggyBank pb = pbService.getPiggyBankByName(dummyPb.getName(), dummyPb.getOwner());

    assertNotNull(pb);
  }

  @Test
  void whenPiggyBankNameOrOwnerDoesNotExists_thenThrowsAnException() {
    given(pbRepository.findByNameAndOwner(any(), any())).willReturn(Optional.empty());

    assertThrows(PiggyBankNotFoundException.class, () -> {
      pbService.getPiggyBankByName(dummyPb.getName(), dummyPb.getOwner());
    });
  }

}