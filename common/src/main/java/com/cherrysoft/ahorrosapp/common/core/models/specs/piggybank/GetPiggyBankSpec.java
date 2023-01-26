package com.cherrysoft.ahorrosapp.common.core.models.specs.piggybank;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class GetPiggyBankSpec {
  private final String ownerUsername;
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private final String pbName;

  public String getPiggyBankName() {
    return pbName;
  }
  
}
