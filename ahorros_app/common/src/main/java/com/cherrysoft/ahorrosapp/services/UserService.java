package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.models.User;
import com.cherrysoft.ahorrosapp.repositories.UserRepository;
import com.cherrysoft.ahorrosapp.services.exceptions.UserNotFoundException;
import com.cherrysoft.ahorrosapp.services.exceptions.UsernameAlreadyTakenException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User getUserByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(username));
  }

  public User addUser(User newUser) {
    checkUsernameAvailability(newUser.getUsername());
    return userRepository.save(newUser);
  }

  public User deleteUser(String username) {
    User user = getUserByUsername(username);
    userRepository.delete(user);
    return user;
  }

  private void checkUsernameAvailability(String username) {
    boolean usernameTaken = userRepository.existsByUsername(username);
    if (usernameTaken) {
      throw new UsernameAlreadyTakenException(username);
    }
  }
}
