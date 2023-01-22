package com.cherrysoft.ahorrosapp.common.repositories;

import com.cherrysoft.ahorrosapp.common.config.RepositoryTestingConfig;
import com.cherrysoft.ahorrosapp.common.core.models.User;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static com.cherrysoft.ahorrosapp.common.config.FakerConfig.FAKER_INSTANCE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes = UserRepository.class)
@Import(RepositoryTestingConfig.class)
class UserRepositoryTest {
  private final Faker faker = FAKER_INSTANCE;
  @Autowired private UserRepository userRepository;

  @BeforeEach
  void setup() {
    User user = User.builder()
        .username("test-username")
        .password(faker.internet().password())
        .build();
    userRepository.save(user);
  }

  @AfterEach
  void deleteAll() {
    userRepository.deleteAll();
  }

  @Nested
  @DisplayName("Find by username method")
  class FindByUsername {

    @Test
    void shouldReturnUser_whenUsernameExists() {
      Optional<User> shouldBePresent = userRepository.findByUsername("test-username");

      assertTrue(shouldBePresent.isPresent());
    }

    @Test
    void shouldReturnEmptyOptional_whenUsernameNotFound() {
      String username = "invalid-username";

      Optional<User> shouldBeEmpty = userRepository.findByUsername(username);

      assertTrue(shouldBeEmpty.isEmpty());
    }

  }

  @Nested
  @DisplayName("Exists by username method")
  class ExistsByUsername {

    @Test
    void shouldReturnTrue_whenUsernameExists() {
      boolean shouldBeTrue = userRepository.existsByUsername("test-username");

      assertTrue(shouldBeTrue);
    }

    @Test
    void shouldReturnFalse_whenUsernameNoExists() {
      boolean shouldBeFalse = userRepository.existsByUsername("invalid-username");

      assertFalse(shouldBeFalse);
    }

  }

}
