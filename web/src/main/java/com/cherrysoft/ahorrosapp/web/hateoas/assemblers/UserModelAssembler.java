package com.cherrysoft.ahorrosapp.web.hateoas.assemblers;

import com.cherrysoft.ahorrosapp.common.core.models.User;
import com.cherrysoft.ahorrosapp.web.controllers.PiggyBankController;
import com.cherrysoft.ahorrosapp.web.controllers.UserController;
import com.cherrysoft.ahorrosapp.web.dtos.UserDTO;
import com.cherrysoft.ahorrosapp.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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
    return linkTo(PiggyBankController.class).withRel("piggy_banks");
  }

}
