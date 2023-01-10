package com.cherrysoft.ahorrosapp.web.dtos;

import com.cherrysoft.ahorrosapp.web.dtos.validation.OnCreate;
import com.cherrysoft.ahorrosapp.web.dtos.validation.PiggyBankName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(itemRelation = "piggybank", collectionRelation = "piggybanks")
public class PiggyBankDTO extends RepresentationModel<PiggyBankDTO> {
  @Null
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private final Long id;

  @PiggyBankName
  @NotBlank(groups = OnCreate.class, message = "A piggy bank name is required.")
  private final String name;

  private final BigDecimal initialAmount;

  @DecimalMin(value = "0.0", inclusive = false)
  private final BigDecimal borrowedAmount;

  private final LocalDate startSavings;

  private final LocalDate endSavings;
}
