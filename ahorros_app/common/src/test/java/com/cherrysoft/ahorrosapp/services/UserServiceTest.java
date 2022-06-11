package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.models.User;
import com.cherrysoft.ahorrosapp.repositories.UserRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.cherrysoft.ahorrosapp.config.FakerConfig.FAKER_INSTANCE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  private final Faker faker = FAKER_INSTANCE;
  @Mock private UserRepository userRepository;
  @InjectMocks private UserService userService;
  private User dummyUser;

  @BeforeEach
  void setUp() {
    dummyUser = User.builder()
        .id(1L)
        .username(faker.name().username())
        .password(faker.internet().password())
        .build();
  }

  @Test
  void whenGivenAnUsername_thenShouldReturnCorrespondingUser() {
    given(userRepository.findByUsername(any(String.class))).willReturn(Optional.of(dummyUser));

    User user = userService.getByUsername(faker.name().username());

    assertNotNull(user);
  }

  @Test
  void whenUsernameNotFound_thenShouldThrowAnException() {
    given(userRepository.findByUsername(any(String.class))).willReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      userService.getByUsername(faker.name().username());
    });
  }
}