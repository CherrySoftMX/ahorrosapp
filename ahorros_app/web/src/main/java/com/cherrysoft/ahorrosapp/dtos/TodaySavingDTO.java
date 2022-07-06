package com.cherrysoft.ahorrosapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Null;
import java.time.LocalDate;

public class TodaySavingDTO extends DailySavingDTO {

  @Override
  @Null
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  public LocalDate getDate() {
    return super.getDate();
  }

}
