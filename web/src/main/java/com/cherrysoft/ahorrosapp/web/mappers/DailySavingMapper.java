package com.cherrysoft.ahorrosapp.web.mappers;

import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.web.dtos.DailySavingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DailySavingMapper {

  DailySaving toDailySaving(DailySavingDTO dailySavingDTO);

  DailySavingDTO toDailySavingDto(DailySaving dailySaving);

}
