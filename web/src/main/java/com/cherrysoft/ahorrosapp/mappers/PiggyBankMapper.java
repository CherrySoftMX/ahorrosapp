package com.cherrysoft.ahorrosapp.mappers;

import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.dtos.PiggyBankDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PiggyBankMapper {

  PiggyBank toPiggyBank(PiggyBankDTO pbDto);

  PiggyBankDTO toPiggyBankDto(PiggyBank pb);

}
