package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.dtos.UserDTO;
import com.cherrysoft.ahorrosapp.mappers.UserMapper;
import com.cherrysoft.ahorrosapp.models.User;
import com.cherrysoft.ahorrosapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping(UserController.BASE_URL)
@AllArgsConstructor
public class UserController {
  public static final String BASE_URL = "/users";
  private final UserService userService;
  private final UserMapper userMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<User> addUser(@RequestBody @Valid UserDTO userDTO) throws URISyntaxException {
    User providedUser = userMapper.userDto2User(userDTO);
    User newUser = userService.addUser(providedUser);
    return ResponseEntity.created(new URI("/users/" + newUser.getId())).body(newUser);
  }
}
