package com.cherrysoft.ahorrosapp.repositories;

import com.cherrysoft.ahorrosapp.models.User;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static com.cherrysoft.ahorrosapp.config.FakerConfig.FAKER_INSTANCE;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {
  private final Faker faker = FAKER_INSTANCE;
  @Autowired private UserRepository userRepository;


  @BeforeEach
  public void setup() {
    userRepository.saveAll(
        List.of(
            new User(1L, "chito", faker.internet().password()),
            new User(2L, "nicolas", faker.internet().password())
        )
    );
  }

  @Test
  public void givenAnExistentUsername_thenReturnsCorrespondingUser() {
    String username = "chito";

    Optional<User> userMaybe = userRepository.findByUsername(username);

    assertTrue(userMaybe.isPresent());
  }

  @Test
  public void givenANonExistentUsername_thenReturnsEmptyUser() {
    String username = "hiking";

    Optional<User> userMaybe = userRepository.findByUsername(username);

    assertTrue(userMaybe.isEmpty());
  }

}