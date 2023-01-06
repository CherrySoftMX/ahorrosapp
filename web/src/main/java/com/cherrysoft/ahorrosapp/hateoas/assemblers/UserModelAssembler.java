package com.cherrysoft.ahorrosapp.hateoas.assemblers;

import com.cherrysoft.ahorrosapp.controllers.PiggyBankController;
import com.cherrysoft.ahorrosapp.controllers.UserController;
import com.cherrysoft.ahorrosapp.core.models.User;
import com.cherrysoft.ahorrosapp.dtos.UserDTO;
import com.cherrysoft.ahorrosapp.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class UserModelAssembler implements RepresentationModelAssembler<User, UserDTO> {
  private final UserMapper userMapper;
  private User user;

  @Override
  public UserDTO toModel(User user) {
    this.user = user;
    UserDTO userModel = userMapper.toUserDto(user);
    userModel.add(List.of(selfLink(), piggyBanksLink()));
    return userModel;
  }

  private Link selfLink() {
    return linkTo(UserController.class)
        .slash(user.getUsername())
        .withSelfRel();
  }

  private Link piggyBanksLink() {
    return linkTo(methodOn(PiggyBankController.class)
        .getPiggyBanks(user.getUsername(), null))
        .withRel("piggybanks");
  }

}
