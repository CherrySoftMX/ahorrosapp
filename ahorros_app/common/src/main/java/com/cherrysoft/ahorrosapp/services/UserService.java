package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.models.User;
import com.cherrysoft.ahorrosapp.repositories.UserRepository;
import com.cherrysoft.ahorrosapp.services.exceptions.UserNotFoundException;
import com.cherrysoft.ahorrosapp.services.exceptions.UsernameAlreadyTakenException;
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

  public User addUser(User user) {
    ensureUniqueUsername(user.getUsername());
    return userRepository.save(user);
  }

  public User deleteUser(String username) {
    User user = getUserByUsername(username);
    userRepository.delete(user);
    return user;
  }

  public User partialUpdateUser(String oldUsername, User updatedUser) {
    String updatedUsername = updatedUser.getUsername();
    if (!oldUsername.equals(updatedUsername)) {
      ensureUniqueUsername(updatedUsername);
    }
    User user = getUserByUsername(oldUsername);
    BeanUtils.copyProperties(updatedUser, user);
    return userRepository.save(user);
  }

  private void ensureUniqueUsername(String username) {
    boolean usernameTaken = userRepository.existsByUsername(username);
    if (usernameTaken) {
      throw new UsernameAlreadyTakenException(username);
    }
  }
}
