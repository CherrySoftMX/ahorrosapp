package com.cherrysoft.ahorrosapp.mappers;

import com.cherrysoft.ahorrosapp.core.IntervalSavingsSummary;
import com.cherrysoft.ahorrosapp.core.PiggyBankSummary;
import com.cherrysoft.ahorrosapp.dtos.summaries.IntervalSavingsSummaryDTO;
import com.cherrysoft.ahorrosapp.dtos.summaries.PiggyBankSummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {DailySavingMapper.class}
)
public interface SavingsSummaryMapper {

  IntervalSavingsSummaryDTO toIntervalSavingsSummaryDto(IntervalSavingsSummary savingsSummary);

  @Mapping(target = "savings", ignore = true)
  PiggyBankSummaryDTO toPiggyBankSummaryDto(PiggyBankSummary savingsSummary);

}
