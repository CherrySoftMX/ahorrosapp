package com.cherrysoft.ahorrosapp.web.security.service;

import com.cherrysoft.ahorrosapp.common.core.models.User;
import com.cherrysoft.ahorrosapp.common.services.UserService;
import com.cherrysoft.ahorrosapp.web.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {
  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      User user = userService.getUserByUsername(username);
      return new SecurityUser(user);
    } catch (UsernameNotFoundException e) {
      throw new BadCredentialsException(e.getMessage());
    }
  }

}
