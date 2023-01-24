package com.cherrysoft.ahorrosapp.web.mappers;

import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.web.dtos.DailySavingDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DailySavingMapper {

  DailySaving toDailySaving(DailySavingDTO dailySavingDTO);

  DailySavingDTO toDailySavingDto(DailySaving dailySaving);

}
