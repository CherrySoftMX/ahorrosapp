package com.cherrysoft.ahorrosapp.hateoas.assemblers;

import com.cherrysoft.ahorrosapp.controllers.PiggyBankController;
import com.cherrysoft.ahorrosapp.controllers.SavingsSummaryController;
import com.cherrysoft.ahorrosapp.core.PiggyBankSummary;
import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.models.User;
import com.cherrysoft.ahorrosapp.dtos.summaries.PiggyBankSummaryDTO;
import com.cherrysoft.ahorrosapp.mappers.SavingsSummaryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class PiggyBankSummaryAssembler implements RepresentationModelAssembler<PiggyBankSummary, PiggyBankSummaryDTO> {
  private final SavingsSummaryMapper savingsSummaryMapper;
  private User owner;
  private PiggyBank pb;
  private PiggyBankSummary pbSummary;

  @Override
  public PiggyBankSummaryDTO toModel(PiggyBankSummary pbSummary) {
    this.pbSummary = pbSummary;
    this.pb = pbSummary.getPiggyBank();
    this.owner = pb.getOwner();
    PiggyBankSummaryDTO summaryModel = savingsSummaryMapper.toPiggyBankSummaryDto(pbSummary);
    summaryModel.add(List.of(selfLink(), piggyBankLink()));
    return summaryModel;
  }

  private Link selfLink() {
    return linkTo(methodOn(SavingsSummaryController.class)
        .getPiggyBankSummaryAsJson(owner.getUsername(), pb.getName()))
        .withSelfRel();
  }

  private Link piggyBankLink() {
    PiggyBank pb = pbSummary.getPiggyBank();
    return linkTo(PiggyBankController.class)
        .slash(pb.getName())
        .withRel("piggy_bank");
  }

}
