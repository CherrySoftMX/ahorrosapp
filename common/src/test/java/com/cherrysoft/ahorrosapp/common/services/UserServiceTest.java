package com.cherrysoft.ahorrosapp.common.services;

import com.cherrysoft.ahorrosapp.common.core.models.User;
import com.cherrysoft.ahorrosapp.common.repositories.UserRepository;
import com.cherrysoft.ahorrosapp.common.services.exceptions.user.UserNotFoundException;
import com.cherrysoft.ahorrosapp.common.services.exceptions.user.UsernameAlreadyTakenException;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.cherrysoft.ahorrosapp.common.config.FakerConfig.FAKER_INSTANCE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  private final Faker faker = FAKER_INSTANCE;
  @Mock private UserRepository userRepository;
  private UserService userService;
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void init() {
    passwordEncoder = new BCryptPasswordEncoder(10);
    userService = new UserService(userRepository, passwordEncoder);
  }

  @Nested
  @DisplayName("Get user")
  class GetUser {

    @Test
    void shouldThrowException_whenUserNotFound() {
      given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());

      assertThrows(UserNotFoundException.class, () -> {
        userService.getUserByUsername(faker.name().username());
      });
    }

  }

  @Nested
  @DisplayName("Create user")
  class CreateUser {

    @Test
    void shouldEncodePassword_whenCreatingUser() {
      User providedUser = User.builder()
          .username("test-username")
          .password("password123")
          .build();
      given(userRepository.existsByUsername(anyString())).willReturn(false);
      given(userRepository.save(providedUser)).willAnswer(invocation -> invocation.getArgument(0));

      User newUser = userService.createUser(providedUser);

      assertTrue(passwordEncoder.matches("password123", newUser.getPassword()));
    }

    @Test
    void shouldThrowException_whenUsernameAlreadyTaken() {
      User providedUser = User.builder()
          .username(faker.name().username())
          .password(faker.internet().password())
          .build();
      given(userRepository.existsByUsername(anyString())).willReturn(true);

      assertThrows(UsernameAlreadyTakenException.class, () -> {
        userService.createUser(providedUser);
      });
    }

  }

  @Nested
  @DisplayName("Update user")
  class UpdateUser {

    @Test
    void shouldNoUpdateUsername() {
      User providedUser = User.builder().username("new-username").build();
      User userToUpdate = User.builder().username("test-username").password("password123").build();
      ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
      given(userRepository.findByUsername("test-username")).willReturn(Optional.of(userToUpdate));

      userService.updateUser("test-username", providedUser);

      verify(userRepository).save(userArgumentCaptor.capture());
      User capturedUser = userArgumentCaptor.getValue();
      assertEquals("test-username", capturedUser.getUsername());
    }

    @Test
    void shouldEncodeNewPassword_whenIsProvided() {
      User providedUser = User.builder().password("new-password123").build();
      User userToUpdate = User.builder()
          .username("test-username")
          .password(passwordEncoder.encode("old-password123"))
          .build();
      given(userRepository.findByUsername("test-username")).willReturn(Optional.of(userToUpdate));

      userService.updateUser("test-username", providedUser);

      assertTrue(passwordEncoder.matches("new-password123", userToUpdate.getPassword()));
    }

  }

  @Nested
  @DisplayName("Delete user")
  class DeleteUser {

    @Test
    void shouldDeleteUser() {
      User userToDelete = User.builder()
          .username("test-username")
          .password(passwordEncoder.encode("password12345"))
          .build();
      given(userRepository.findByUsername("test-username")).willReturn(Optional.of(userToDelete));

      User deletedUser = userService.deleteUser("test-username");

      assertNotNull(deletedUser);
      verify(userRepository).delete(userToDelete);
    }

    @Test
    void shouldThrowException_whenUserToDeleteNotFound() {
      given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());

      assertThrows(UserNotFoundException.class, () -> {
        userService.deleteUser(faker.name().username());
      });
    }

  }

}
