package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.core.models.User;
import com.cherrysoft.ahorrosapp.dtos.UserDTO;
import com.cherrysoft.ahorrosapp.dtos.validation.OnCreate;
import com.cherrysoft.ahorrosapp.hateoas.assemblers.UserModelAssembler;
import com.cherrysoft.ahorrosapp.mappers.UserMapper;
import com.cherrysoft.ahorrosapp.security.SecurityUser;
import com.cherrysoft.ahorrosapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
  @PreAuthorize("#loggedUser.username == #username")
  public UserDTO getUserByUsername(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String username
  ) {
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
        .created(URI.create(String.format("%s/%s", BASE_URL, result.getId())))
        .body(userModelAssembler.toModel(result));
  }

  @PatchMapping("/{username}")
  @PreAuthorize("#loggedUser.username == #username")
  public UserDTO updateUser(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String username,
      @RequestBody @Valid UserDTO payload
  ) {
    User updatedUser = userMapper.toUser(payload);
    User result = userService.updateUser(username, updatedUser);
    return userModelAssembler.toModel(result);
  }

  @DeleteMapping("/{username}")
  @PreAuthorize("#loggedUser.username == #username")
  public ResponseEntity<UserDTO> deleteUser(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String username
  ) {
    User result = userService.deleteUser(username);
    return ResponseEntity.ok(userMapper.toUserDto(result));
  }

}
