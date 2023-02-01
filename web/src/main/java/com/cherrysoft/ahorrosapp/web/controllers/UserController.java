package com.cherrysoft.ahorrosapp.web.controllers;

import com.cherrysoft.ahorrosapp.common.core.models.User;
import com.cherrysoft.ahorrosapp.common.services.UserService;
import com.cherrysoft.ahorrosapp.web.dtos.UserDTO;
import com.cherrysoft.ahorrosapp.web.hateoas.assemblers.UserModelAssembler;
import com.cherrysoft.ahorrosapp.web.mappers.UserMapper;
import com.cherrysoft.ahorrosapp.web.security.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cherrysoft.ahorrosapp.web.utils.ApiDocsConstants.*;

@RestController
@RequestMapping(UserController.BASE_URL)
@Validated
@RequiredArgsConstructor
@Tag(name = "Users", description = "API to manage users")
@ApiResponses({
    @ApiResponse(ref = BAD_REQUEST_RESPONSE_REF, responseCode = "400"),
    @ApiResponse(ref = UNAUTHORIZED_RESPONSE_REF, responseCode = "401"),
    @ApiResponse(ref = NOT_FOUND_RESPONSE_REF, responseCode = "404"),
    @ApiResponse(ref = INTERNAL_SERVER_ERROR_RESPONSE_REF, responseCode = "500")
})
public class UserController {
  public static final String BASE_URL = "/users";
  private final UserService userService;
  private final UserMapper userMapper;
  private final UserModelAssembler userModelAssembler;

  @Operation(summary = "Returns the user with the given username")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User found", content = {
          @Content(schema = @Schema(implementation = UserDTO.class))
      }),
      @ApiResponse(ref = NOT_FOUND_RESPONSE_REF, responseCode = "404")
  })
  @GetMapping("/{username}")
  @PreAuthorize("#loggedUser.username == #username")
  public UserDTO getUserByUsername(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String username
  ) {
    User result = userService.getUserByUsername(username);
    return userModelAssembler.toModel(result);
  }

  @Operation(summary = "Partially updates a user with the given payload")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User updated", content = {
          @Content(schema = @Schema(implementation = UserDTO.class))
      }),
      @ApiResponse(ref = NOT_FOUND_RESPONSE_REF, responseCode = "404")
  })
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

  @Operation(summary = "Deletes a user with the given ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User deleted", content = {
          @Content(schema = @Schema(implementation = UserDTO.class))
      }),
      @ApiResponse(ref = NOT_FOUND_RESPONSE_REF, responseCode = "404")
  })
  @DeleteMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("#loggedUser.username == #username")
  public ResponseEntity<UserDTO> deleteUser(
      @AuthenticationPrincipal SecurityUser loggedUser,
      @PathVariable String username
  ) {
    User result = userService.deleteUser(username);
    return ResponseEntity.ok(userMapper.toUserDto(result));
  }

}
