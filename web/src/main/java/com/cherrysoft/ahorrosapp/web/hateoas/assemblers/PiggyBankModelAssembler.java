package com.cherrysoft.ahorrosapp.web.hateoas.assemblers;

import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.models.User;
import com.cherrysoft.ahorrosapp.web.controllers.PiggyBankController;
import com.cherrysoft.ahorrosapp.web.controllers.UserController;
import com.cherrysoft.ahorrosapp.web.dtos.PiggyBankDTO;
import com.cherrysoft.ahorrosapp.web.mappers.PiggyBankMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
@RequiredArgsConstructor
public class PiggyBankModelAssembler implements RepresentationModelAssembler<PiggyBank, PiggyBankDTO> {
  private final PiggyBankMapper pbMapper;
  private PiggyBank pb;

  @Override
  public PiggyBankDTO toModel(PiggyBank pb) {
    this.pb = pb;
    PiggyBankDTO pbModel = pbMapper.toPiggyBankDto(pb);
    pbModel.add(List.of(selfLink(), ownerLink()));
    return pbModel;
  }

  private Link selfLink() {
    return linkTo(PiggyBankController.class)
        .slash(pb.getName())
        .withSelfRel();
  }

  private Link ownerLink() {
    User owner = pb.getOwner();
    return linkTo(UserController.class)
        .slash(owner.getUsername())
        .withRel("owner");
  }

}
