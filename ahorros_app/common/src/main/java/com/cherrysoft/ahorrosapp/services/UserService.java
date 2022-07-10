package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.core.models.User;
import com.cherrysoft.ahorrosapp.repositories.UserRepository;
import com.cherrysoft.ahorrosapp.services.exceptions.user.UserNotFoundException;
import com.cherrysoft.ahorrosapp.services.exceptions.user.UsernameAlreadyTakenException;
import com.cherrysoft.ahorrosapp.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public User getUserByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(username));
  }

  public User createUser(User user) {
    ensureUniqueUsername(user.getUsername());
    return userRepository.save(user);
  }

  public User partialUpdateUser(String oldUsername, User updatedUser) {
    String newUsername = updatedUser.getUsername();
    if (!oldUsername.equals(newUsername)) {
      ensureUniqueUsername(newUsername);
    }
    User user = getUserByUsername(oldUsername);
    BeanUtils.copyProperties(updatedUser, user);
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
