package com.cherrysoft.ahorrosapp.mappers;

import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.dtos.PiggyBankDTO;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring"
)
public interface PiggyBankMapper {

  PiggyBank toPiggyBank(PiggyBankDTO pbDto);

  PiggyBankDTO toPiggyBankDto(PiggyBank pb);

}
