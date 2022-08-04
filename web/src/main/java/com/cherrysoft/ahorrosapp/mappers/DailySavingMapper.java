package com.cherrysoft.ahorrosapp.mappers;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.dtos.DailySavingDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DailySavingMapper {

  DailySaving toDailySaving(DailySavingDTO dailySavingDTO);

  DailySavingDTO toDailySavingDto(DailySaving dailySaving);

}
