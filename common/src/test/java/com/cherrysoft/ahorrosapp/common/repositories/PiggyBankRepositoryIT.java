package com.cherrysoft.ahorrosapp.common.repositories;

import com.cherrysoft.ahorrosapp.common.config.RepositoryTestingConfig;
import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.core.models.SavingsDateRange;
import com.cherrysoft.ahorrosapp.common.core.models.User;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static com.cherrysoft.ahorrosapp.common.config.FakerConfig.FAKER_INSTANCE;
import static com.cherrysoft.ahorrosapp.common.utils.DateUtils.today;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = {PiggyBankRepository.class, UserRepository.class})
@Import(RepositoryTestingConfig.class)
@ActiveProfiles("test")
class PiggyBankRepositoryIT {
  private final Faker faker = FAKER_INSTANCE;
  @Autowired private UserRepository userRepository;
  @Autowired private PiggyBankRepository pbRepository;

  @BeforeEach
  void setUp() {
    PiggyBank pb = PiggyBank.builder()
        .name("test-piggy")
        .savingsDateRange(new SavingsDateRange(today(), today().plusDays(7)))
        .build();

    User user = User.builder()
        .username("test-username")
        .password(faker.internet().password())
        .build();

    user.addPiggyBank(pb);
    userRepository.saveAndFlush(user);
  }

  @AfterEach
  void deleteAll() {
    pbRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Nested
  @DisplayName("Find by owner's username method")
  class FindByOwnerUsername {

    @Test
    void shouldReturnOnePiggyBank() {
      Page<PiggyBank> pbs = pbRepository.findAllByOwnerUsername("test-username", Pageable.ofSize(10));

      assertEquals(1, pbs.getContent().size());
    }

    @Test
    void shouldReturnOneTotalPage() {
      Page<PiggyBank> pbs = pbRepository.findAllByOwnerUsername("test-username", Pageable.ofSize(10));

      assertEquals(1, pbs.getTotalPages());
    }

    @Test
    void shouldReturnZeroPiggyBanks_whenOwnerUsernameNotFound() {
      Page<PiggyBank> pbs = pbRepository.findAllByOwnerUsername("invalid-username", Pageable.ofSize(10));

      assertEquals(0, pbs.getContent().size());
    }

  }

  @Nested
  @DisplayName("Find by piggy bank name and owner method")
  class FindByNameAndOwner {

    @Test
    void shouldReturnPiggyBank() {
      User owner = userRepository.findByUsername("test-username").orElseThrow();
      Optional<PiggyBank> maybePb = pbRepository.findByNameAndOwner("test-piggy", owner);

      assertTrue(maybePb.isPresent());
    }

    @Test
    void shouldReturnEmptyOptionalPiggyBank_whenOwnerDoesNotHaveAnyPiggyBanks() {
      User anotherUser = User.builder()
          .username("another-username")
          .password(faker.internet().password())
          .build();
      anotherUser = userRepository.save(anotherUser);

      Optional<PiggyBank> maybePb = pbRepository.findByNameAndOwner("test-piggy", anotherUser);

      assertTrue(maybePb.isEmpty());
    }

  }

  @Nested
  @DisplayName("Exists by piggy bank name and owner")
  class ExistsByNameAndOwner {

    @Test
    void shouldReturnTrue_whenPiggyBankExistsAndOwnerOwnsThePiggyBank() {
      User owner = userRepository.findByUsername("test-username").orElseThrow();
      boolean shouldBeTrue = pbRepository.existsByNameAndOwner("test-piggy", owner);

      assertTrue(shouldBeTrue);
    }

    @Test
    void shouldReturnFalse_whenOwnerDoesNotHaveAnyPiggyBank() {
      User userWithoutPbs = User.builder()
          .username("another-username")
          .password(faker.internet().password())
          .build();
      userWithoutPbs = userRepository.save(userWithoutPbs);

      boolean shouldBeFalse = pbRepository.existsByNameAndOwner("test-piggy", userWithoutPbs);

      assertFalse(shouldBeFalse);
    }

    @Test
    void shouldReturnFalse_whenPiggyBankDoesNotExists() {
      User owner = userRepository.findByUsername("test-username").orElseThrow();
      boolean shouldBeFalse = pbRepository.existsByNameAndOwner("invalid-piggy", owner);

      assertFalse(shouldBeFalse);
    }

  }

}
