package com.cherrysoft.ahorrosapp.web.mappers;

import com.cherrysoft.ahorrosapp.common.core.IntervalSavingsSummary;
import com.cherrysoft.ahorrosapp.common.core.PiggyBankSummary;
import com.cherrysoft.ahorrosapp.web.dtos.summaries.IntervalSavingsSummaryDTO;
import com.cherrysoft.ahorrosapp.web.dtos.summaries.PiggyBankSummaryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DailySavingMapper.class})
public interface SavingsSummaryMapper {

  IntervalSavingsSummaryDTO toIntervalSavingsSummaryDto(IntervalSavingsSummary savingsSummary);

  PiggyBankSummaryDTO toPiggyBankSummaryDto(PiggyBankSummary savingsSummary);

}
