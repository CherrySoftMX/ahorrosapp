package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.dtos.UserDTO;
import com.cherrysoft.ahorrosapp.dtos.validation.OnCreate;
import com.cherrysoft.ahorrosapp.mappers.UserMapper;
import com.cherrysoft.ahorrosapp.models.User;
import com.cherrysoft.ahorrosapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping(UserController.BASE_URL)
@Validated
@AllArgsConstructor
public class UserController {
  public static final String BASE_URL = "/users";
  private final UserService userService;
  private final UserMapper userMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Validated(OnCreate.class)
  public ResponseEntity<UserDTO> addUser(@RequestBody @Valid UserDTO userDTO) throws URISyntaxException {
    User providedUser = userMapper.userDto2User(userDTO);
    User newUser = userService.addUser(providedUser);
    return ResponseEntity
        .created(new URI("/users/" + newUser.getId()))
        .body(userMapper.user2UserDto(newUser));
  }

  @PatchMapping("/{username}")
  public ResponseEntity<UserDTO> partialUpdateUser(
      @PathVariable String username,
      @RequestBody @Valid UserDTO userDTO
  ) {
    User partialUpdateUser = userMapper.userDto2User(userDTO);
    User updatedUser = userService.partialUpdateUser(username, partialUpdateUser);
    return ResponseEntity.ok(userMapper.user2UserDto(updatedUser));
  }

  @DeleteMapping("/{username}")
  public ResponseEntity<UserDTO> deleteUser(@PathVariable String username) {
    User deletedUser = userService.deleteUser(username);
    return ResponseEntity.ok(userMapper.user2UserDto(deletedUser));
  }

}
