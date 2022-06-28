package com.cherrysoft.ahorrosapp.mappers;

import com.cherrysoft.ahorrosapp.dtos.PiggyBankDTO;
import com.cherrysoft.ahorrosapp.models.PiggyBank;
import com.cherrysoft.ahorrosapp.models.SavingInterval;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
    componentModel = "spring",
    imports = {SavingInterval.class}
)
public interface PiggyBankMapper {

  @Mapping(
      target = "savingInterval",
      expression = "java( new SavingInterval(pbDto.getStartSavings(), pbDto.getEndSavings()) )"
  )
  PiggyBank toPiggyBank(PiggyBankDTO pbDto);

  @Mappings({
      @Mapping(
          target = "startSavings",
          expression = "java( pb.getSavingInterval().getStartDate() )"
      ),
      @Mapping(
          target = "endSavings",
          expression = "java( pb.getSavingInterval().getEndDate() )"
      )
  })
  PiggyBankDTO toPiggyBankDto(PiggyBank pb);

}
