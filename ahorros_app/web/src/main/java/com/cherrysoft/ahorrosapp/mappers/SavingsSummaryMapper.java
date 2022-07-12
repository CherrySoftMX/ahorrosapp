package com.cherrysoft.ahorrosapp.mappers;

import com.cherrysoft.ahorrosapp.core.SavingsSummary;
import com.cherrysoft.ahorrosapp.dtos.SavingsSummaryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DailySavingMapper.class})
public interface SavingsSummaryMapper {

  SavingsSummaryDTO toSavingsSummaryDto(SavingsSummary savingsSummary);

}
