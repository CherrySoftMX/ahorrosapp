package com.cherrysoft.ahorrosapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TodaySavingDTO extends DailySavingDTO {

  public TodaySavingDTO(Long id, BigDecimal amount, LocalDate date, String description) {
    super(id, amount, date, description);
  }

  @Override
  @Null
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  public LocalDate getDate() {
    return super.getDate();
  }

}
