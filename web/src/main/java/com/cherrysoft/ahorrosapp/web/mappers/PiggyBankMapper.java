package com.cherrysoft.ahorrosapp.web.mappers;

import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.web.dtos.PiggyBankDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface PiggyBankMapper {

  PiggyBank toPiggyBank(PiggyBankDTO pbDto);

  PiggyBankDTO toPiggyBankDto(PiggyBank pb);

}
