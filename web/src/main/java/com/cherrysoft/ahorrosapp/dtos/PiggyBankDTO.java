package com.cherrysoft.ahorrosapp.dtos;

import com.cherrysoft.ahorrosapp.dtos.validation.OnCreate;
import com.cherrysoft.ahorrosapp.dtos.validation.PiggyBankName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PiggyBankDTO {
  @Null
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private final Long id;

  @PiggyBankName
  @NotBlank(groups = OnCreate.class, message = "A piggy bank name is required.")
  private final String name;

  private final BigDecimal initialAmount;

  private final BigDecimal borrowedAmount;

  private final LocalDate startSavings;

  private final LocalDate endSavings;

}
