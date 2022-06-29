package com.cherrysoft.ahorrosapp.mappers;

import com.cherrysoft.ahorrosapp.dtos.PiggyBankDTO;
import com.cherrysoft.ahorrosapp.models.PiggyBank;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring"
)
public interface PiggyBankMapper {

  PiggyBank toPiggyBank(PiggyBankDTO pbDto);

  PiggyBankDTO toPiggyBankDto(PiggyBank pb);

}
