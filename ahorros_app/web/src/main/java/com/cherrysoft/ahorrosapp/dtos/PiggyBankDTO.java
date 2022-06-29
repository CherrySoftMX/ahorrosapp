package com.cherrysoft.ahorrosapp.dtos;

import com.cherrysoft.ahorrosapp.dtos.validation.OnCreate;
import com.cherrysoft.ahorrosapp.dtos.validation.PiggyBankName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PiggyBankDTO {
  @Null
  private Long id;

  @Size(
      min = 5,
      max = 30,
      message = "Piggy bank name must be between 5 and 30 chars."
  )
  @PiggyBankName
  @NotBlank(groups = OnCreate.class, message = "A piggy bank name is required.")
  private String name;

  private LocalDate startSavings;

  private LocalDate endSavings;
}
