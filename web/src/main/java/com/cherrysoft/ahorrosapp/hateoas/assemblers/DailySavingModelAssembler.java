package com.cherrysoft.ahorrosapp.hateoas.assemblers;

import com.cherrysoft.ahorrosapp.controllers.DailySavingController;
import com.cherrysoft.ahorrosapp.controllers.PiggyBankController;
import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.dtos.DailySavingDTO;
import com.cherrysoft.ahorrosapp.mappers.DailySavingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class DailySavingModelAssembler implements RepresentationModelAssembler<DailySaving, DailySavingDTO> {
  private final DailySavingMapper dailySavingMapper;
  private PiggyBank pb;
  private DailySaving dailySaving;

  @Override
  public DailySavingDTO toModel(DailySaving dailySaving) {
    this.dailySaving = dailySaving;
    this.pb = dailySaving.getPiggyBank();
    DailySavingDTO dailySavingModel = dailySavingMapper.toDailySavingDto(dailySaving);
    dailySavingModel.add(List.of(selfLink(), piggyBankLink()));
    return dailySavingModel;
  }

  public Link selfLink() {
    return linkTo(methodOn(DailySavingController.class)
        .getDailySaving(null, pb.getName(), dailySaving.getDate()))
        .withSelfRel();
  }

  public Link piggyBankLink() {
    return linkTo(PiggyBankController.class)
        .slash(pb.getName())
        .withRel("piggy_bank");
  }

}
