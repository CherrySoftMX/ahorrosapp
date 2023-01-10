package com.cherrysoft.ahorrosapp.common.repositories;

import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.config.RepositoryTestingConfig;
import com.cherrysoft.ahorrosapp.common.core.models.User;
import com.cherrysoft.ahorrosapp.common.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes = {PiggyBankRepository.class, UserRepository.class})
@Import({RepositoryTestingConfig.class})
class PiggyBankRepositoryTest {
  @Autowired private PiggyBankRepository piggyBankRepository;
  @Autowired private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    List<User> users = TestUtils.Users.newUsers();
    userRepository.saveAll(users);
    User owner = userRepository.findAll().get(0);

    PiggyBank piggyBank = TestUtils.PiggyBanks.newPiggyBank();
    piggyBank.setOwner(owner);
    piggyBankRepository.save(piggyBank);
  }

  @Test
  void whenPiggyBankAndOwnerExist_thenPiggyBankExistsAndIsPresent() {
    String piggyBankName = "piggy";
    User owner = userRepository.findByUsername("javier").get();

    boolean shouldBeTrue = piggyBankRepository.existsByNameAndOwner(piggyBankName, owner);
    Optional<PiggyBank> shouldBePresent = piggyBankRepository.findByNameAndOwner(piggyBankName, owner);

    assertTrue(shouldBeTrue);
    assertTrue(shouldBePresent.isPresent());
  }

  @Test
  void whenPiggyBankNameDoesNotExist_thenPiggyBankDoesNotExistsAndIsNotPresent() {
    String piggyBankName = "my piggy";
    User owner = userRepository.findByUsername("javier").get();

    boolean shouldBeFalse = piggyBankRepository.existsByNameAndOwner(piggyBankName, owner);
    Optional<PiggyBank> shouldBeEmpty = piggyBankRepository.findByNameAndOwner(piggyBankName, owner);

    assertFalse(shouldBeFalse);
    assertTrue(shouldBeEmpty.isEmpty());
  }

  @Test
  void whenOwnerDoesNotHavePiggyBank_thenPiggyBankDoesNotExistsAndIsNotPresent() {
    String piggyBankName = "piggy";
    User owner = userRepository.findByUsername("nicolas").get();

    boolean shouldBeFalse = piggyBankRepository.existsByNameAndOwner(piggyBankName, owner);
    Optional<PiggyBank> shouldBeEmpty = piggyBankRepository.findByNameAndOwner(piggyBankName, owner);

    assertFalse(shouldBeFalse);
    assertTrue(shouldBeEmpty.isEmpty());
  }

}