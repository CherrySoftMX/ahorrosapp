package com.cherrysoft.ahorrosapp.common.services;

import com.cherrysoft.ahorrosapp.common.core.models.User;
import com.cherrysoft.ahorrosapp.common.repositories.UserRepository;
import com.cherrysoft.ahorrosapp.common.services.exceptions.user.UserNotFoundException;
import com.cherrysoft.ahorrosapp.common.services.exceptions.user.UsernameAlreadyTakenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User getUserByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(username));
  }

  public void ensureUserExistByUsername(String username) {
    if (!userRepository.existsByUsername(username)) {
      throw new UserNotFoundException(username);
    }
  }

  public User createUser(User user) {
    ensureUniqueUsername(user.getUsername());
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  // For now, only the user's password can be updated.
  public User updateUser(String username, User updatedUser) {
    User user = getUserByUsername(username);
    if (nonNull(updatedUser.getPassword())) {
      user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
    }
    return userRepository.save(user);
  }

  public User deleteUser(String username) {
    User user = getUserByUsername(username);
    userRepository.delete(user);
    return user;
  }

  private void ensureUniqueUsername(String username) {
    boolean usernameTaken = userRepository.existsByUsername(username);
    if (usernameTaken) {
      throw new UsernameAlreadyTakenException(username);
    }
  }

}
