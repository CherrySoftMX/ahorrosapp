package com.cherrysoft.ahorrosapp.web.hateoas.assemblers;

import com.cherrysoft.ahorrosapp.core.PiggyBankSummary;
import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.web.controllers.PiggyBankController;
import com.cherrysoft.ahorrosapp.web.controllers.SavingsSummaryController;
import com.cherrysoft.ahorrosapp.web.dtos.summaries.PiggyBankSummaryDTO;
import com.cherrysoft.ahorrosapp.web.mappers.SavingsSummaryMapper;
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
  private PiggyBank pb;

  @Override
  public PiggyBankSummaryDTO toModel(PiggyBankSummary pbSummary) {
    this.pb = pbSummary.getPiggyBank();
    PiggyBankSummaryDTO summaryModel = savingsSummaryMapper.toPiggyBankSummaryDto(pbSummary);
    summaryModel.add(List.of(selfLink(), piggyBankLink()));
    return summaryModel;
  }

  private Link selfLink() {
    return linkTo(methodOn(SavingsSummaryController.class)
        .getPiggyBankSummaryAsJson(null, pb.getName()))
        .withSelfRel();
  }

  private Link piggyBankLink() {
    return linkTo(PiggyBankController.class)
        .slash(pb.getName())
        .withRel("piggy_bank");
  }

}
