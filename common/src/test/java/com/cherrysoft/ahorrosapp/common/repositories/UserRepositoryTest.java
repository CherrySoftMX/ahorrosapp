package com.cherrysoft.ahorrosapp.common.repositories;

import com.cherrysoft.ahorrosapp.common.config.RepositoryTestingConfig;
import com.cherrysoft.ahorrosapp.common.core.models.User;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static com.cherrysoft.ahorrosapp.common.config.FakerConfig.FAKER_INSTANCE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes = {UserRepository.class})
@Import({RepositoryTestingConfig.class})
class UserRepositoryTest {
  private final Faker faker = FAKER_INSTANCE;
  @Autowired private UserRepository userRepository;

  @BeforeEach
  void setup() {
    userRepository.saveAll(
        List.of(
            new User(1L, "chito", faker.internet().password()),
            new User(2L, "nicolas", faker.internet().password())
        )
    );
  }

  @Test
  void givenAnExistentUsername_thenReturnsCorrespondingUser() {
    String username = "chito";

    Optional<User> shouldBePresent = userRepository.findByUsername(username);

    assertTrue(shouldBePresent.isPresent());
  }

  @Test
  void givenANonExistentUsername_thenReturnsEmptyUser() {
    String username = "hiking";

    Optional<User> shouldBeEmpty = userRepository.findByUsername(username);

    assertTrue(shouldBeEmpty.isEmpty());
  }

  @Test
  void testExistsByUsername() {
    String existentUsername = "nicolas";
    String nonExistentUsername = "javier";

    boolean shouldBeTrue = userRepository.existsByUsername(existentUsername);

    assertTrue(shouldBeTrue);

    boolean shouldBeFalse = userRepository.existsByUsername(nonExistentUsername);

    assertFalse(shouldBeFalse);
  }

}
