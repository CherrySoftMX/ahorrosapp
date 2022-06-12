package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.models.User;
import com.cherrysoft.ahorrosapp.repositories.UserRepository;
import com.cherrysoft.ahorrosapp.services.exceptions.UserNotFoundException;
import com.cherrysoft.ahorrosapp.services.exceptions.UsernameAlreadyTakenException;
import com.cherrysoft.ahorrosapp.utils.TestUtils;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.cherrysoft.ahorrosapp.config.FakerConfig.FAKER_INSTANCE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  private final Faker faker = FAKER_INSTANCE;
  @Mock private UserRepository userRepository;
  @InjectMocks private UserService userService;

  @Test
  void whenGivenAnUsername_thenShouldReturnCorrespondingUser() {
    User user = TestUtils.getUser();
    String username = user.getUsername();
    given(userRepository.findByUsername(username)).willReturn(Optional.of(user));

    user = userService.getUserByUsername(username);

    assertNotNull(user);
  }

  @Test
  void whenUsernameNotFound_thenShouldThrowAnException() {
    given(userRepository.findByUsername(any(String.class))).willReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      userService.getUserByUsername(faker.name().username());
    });
  }

  @Test
  void whenUserSuccessfullyAdded_thenShouldReturnAddedUser() {
    User newUser = TestUtils.getUser();
    given(userRepository.save(newUser)).willAnswer(invocationOnMock -> User.builder()
        .id(1L)
        .username(newUser.getUsername())
        .password(newUser.getPassword())
        .build()
    );

    User addedUser = userService.addUser(newUser);

    assertNotNull(addedUser.getId());
    assertNotEquals(newUser, addedUser);
  }

  @Test
  void whenUsernameAlreadyTaken_thenShouldThrowAnException() {
    User newUser = TestUtils.getUser();
    given(userRepository.existsByUsername(any(String.class))).willReturn(true);

    assertThrows(UsernameAlreadyTakenException.class, () -> {
      userService.addUser(newUser);
    });
    verify(userRepository, never()).save(any());
  }

  @Test
  void whenUserSuccessfullyDeleted_thenShouldReturnDeletedUser() {
    User userToDelete = TestUtils.getUser();
    String username = userToDelete.getUsername();
    given(userRepository.findByUsername(username)).willReturn(Optional.of(userToDelete));

    User deletedUser = userService.deleteUser(username);

    assertNotNull(deletedUser);
  }

  @Test
  void whenUsernameOfUserToDeleteIsNotFound_thenShouldThrowAnException() {
    given(userRepository.findByUsername(any(String.class))).willReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      userService.deleteUser(faker.name().username());
    });
    verify(userRepository, never()).delete(any(User.class));
  }

}