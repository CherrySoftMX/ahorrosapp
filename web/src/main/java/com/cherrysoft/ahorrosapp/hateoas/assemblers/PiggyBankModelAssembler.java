package com.cherrysoft.ahorrosapp.hateoas.assemblers;

import com.cherrysoft.ahorrosapp.controllers.PiggyBankController;
import com.cherrysoft.ahorrosapp.controllers.UserController;
import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.models.User;
import com.cherrysoft.ahorrosapp.dtos.PiggyBankDTO;
import com.cherrysoft.ahorrosapp.mappers.PiggyBankMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class PiggyBankModelAssembler implements RepresentationModelAssembler<PiggyBank, PiggyBankDTO> {
  private final PiggyBankMapper pbMapper;
  private User owner;
  private PiggyBank pb;

  @Override
  public PiggyBankDTO toModel(PiggyBank pb) {
    this.pb = pb;
    this.owner = pb.getOwner();
    PiggyBankDTO pbModel = pbMapper.toPiggyBankDto(pb);
    pbModel.add(List.of(selfLink(), ownerLink()));
    return pbModel;
  }

  private Link selfLink() {
    return linkTo(methodOn(PiggyBankController.class)
        .getPiggyBank(owner.getUsername(), pb.getName()))
        .withSelfRel();
  }

  private Link ownerLink() {
    return linkTo(UserController.class)
        .slash(owner.getUsername())
        .withRel("owner");
  }

}
