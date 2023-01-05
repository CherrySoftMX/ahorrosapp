package com.cherrysoft.ahorrosapp.dtos;

import com.cherrysoft.ahorrosapp.dtos.validation.OnCreate;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Relation(itemRelation = "saving", collectionRelation = "savings")
public class DailySavingDTO extends RepresentationModel<DailySavingDTO> {
  @Null
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private final Long id;

  @NotNull(groups = OnCreate.class)
  private final BigDecimal amount;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private final LocalDate date;

  private final String description;
}
