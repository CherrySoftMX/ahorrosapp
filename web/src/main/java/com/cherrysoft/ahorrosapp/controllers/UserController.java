package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.core.models.User;
import com.cherrysoft.ahorrosapp.dtos.UserDTO;
import com.cherrysoft.ahorrosapp.dtos.validation.OnCreate;
import com.cherrysoft.ahorrosapp.hateoas.assemblers.UserModelAssembler;
import com.cherrysoft.ahorrosapp.mappers.UserMapper;
import com.cherrysoft.ahorrosapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(UserController.BASE_URL)
@Validated
@RequiredArgsConstructor
public class UserController {
  public static final String BASE_URL = "/users";
  private final UserService userService;
  private final UserMapper userMapper;
  private final UserModelAssembler userModelAssembler;

  @GetMapping("/{username}")
  public UserDTO getUserByUsername(@PathVariable String username) {
    User result = userService.getUserByUsername(username);
    return userModelAssembler.toModel(result);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Validated(OnCreate.class)
  public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO payload) {
    User providedUser = userMapper.toUser(payload);
    User result = userService.createUser(providedUser);
    return ResponseEntity
        .created(URI.create("/users/" + result.getId()))
        .body(userModelAssembler.toModel(result));
  }

  @PatchMapping("/{username}")
  public UserDTO updateUser(
      @PathVariable String username,
      @RequestBody @Valid UserDTO payload
  ) {
    User partialUpdatedUser = userMapper.toUser(payload);
    User result = userService.updateUser(username, partialUpdatedUser);
    return userModelAssembler.toModel(result);
  }

  @DeleteMapping("/{username}")
  public ResponseEntity<UserDTO> deleteUser(@PathVariable String username) {
    User result = userService.deleteUser(username);
    return ResponseEntity.ok(userMapper.toUserDto(result));
  }

}
