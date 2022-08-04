package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.core.models.User;
import com.cherrysoft.ahorrosapp.repositories.UserRepository;
import com.cherrysoft.ahorrosapp.services.exceptions.user.UserNotFoundException;
import com.cherrysoft.ahorrosapp.services.exceptions.user.UsernameAlreadyTakenException;
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
  void whenGivenAnExistentUsername_thenReturnsCorrespondingUser() {
    User user = TestUtils.Users.newUser();
    String username = user.getUsername();
    given(userRepository.findByUsername(username)).willReturn(Optional.of(user));

    user = userService.getUserByUsername(username);

    assertNotNull(user);
  }

  @Test
  void whenUsernameNotFound_thenThrowsAnException() {
    given(userRepository.findByUsername(any())).willReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      userService.getUserByUsername(faker.name().username());
    });
  }

  @Test
  void whenUserSuccessfullyAdded_thenReturnsAddedUser() {
    User newUser = TestUtils.Users.newUser();
    given(userRepository.save(newUser)).willAnswer(invocationOnMock -> User.builder()
        .id(1L)
        .username(newUser.getUsername())
        .password(newUser.getPassword())
        .build()
    );

    User addedUser = userService.createUser(newUser);

    assertNotNull(addedUser.getId());
    assertNotEquals(newUser, addedUser);
  }

  @Test
  void whenAddingAUserWithAnUsernameAlreadyTaken_thenThrowsAnException() {
    User newUser = TestUtils.Users.newUser();
    given(userRepository.existsByUsername(any())).willReturn(true);

    assertThrows(UsernameAlreadyTakenException.class, () -> {
      userService.createUser(newUser);
    });
    verify(userRepository, never()).save(any());
  }

  @Test
  void whenPartialUpdatingUser_thenUpdatesOnlyNonNullProperties() {
    User savedUser = TestUtils.Users.savedUser();
    User partialUpdateUser = User.builder().username(faker.name().username()).build();
    User updatedUser = User.builder()
        .id(savedUser.getId())
        .username(partialUpdateUser.getUsername())
        .password(savedUser.getPassword())
        .build();
    given(userRepository.findByUsername(savedUser.getUsername())).willReturn(Optional.of(savedUser));
    given(userRepository.save(updatedUser)).willReturn(updatedUser);

    User resultUpdatedUser = userService.partialUpdateUser(savedUser.getUsername(), partialUpdateUser);

    assertEquals(updatedUser, resultUpdatedUser);
    assertEquals(savedUser.getId(), resultUpdatedUser.getId());
    assertEquals(savedUser.getPassword(), resultUpdatedUser.getPassword());
  }

  @Test
  void whenUpdatingUserWithAlreadyTakenUsername_thenThrowsAnException() {
    User savedUser = TestUtils.Users.savedUser();
    User partialUpdateUser = User.builder().username(faker.name().username()).build();
    given(userRepository.existsByUsername(any())).willReturn(true);

    assertThrows(UsernameAlreadyTakenException.class, () -> {
      userService.partialUpdateUser(savedUser.getUsername(), partialUpdateUser);
    });
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void whenUserSuccessfullyDeleted_thenReturnsDeletedUser() {
    User userToDelete = TestUtils.Users.newUser();
    String username = userToDelete.getUsername();
    given(userRepository.findByUsername(username)).willReturn(Optional.of(userToDelete));

    User deletedUser = userService.deleteUser(username);

    assertNotNull(deletedUser);
  }

  @Test
  void whenUsernameOfUserToDeleteIsNotFound_thenThrowsAnException() {
    given(userRepository.findByUsername(any(String.class))).willReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      userService.deleteUser(faker.name().username());
    });
    verify(userRepository, never()).delete(any(User.class));
  }
}