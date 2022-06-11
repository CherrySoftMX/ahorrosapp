package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.models.User;
import com.cherrysoft.ahorrosapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User getByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(username));
  }
}
