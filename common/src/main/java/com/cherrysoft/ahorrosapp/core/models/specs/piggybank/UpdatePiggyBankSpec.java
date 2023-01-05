package com.cherrysoft.ahorrosapp.core.models.specs.piggybank;

import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdatePiggyBankSpec {
  private final String ownerUsername;
  private final String oldPbName;
  private final PiggyBank updatedPb;
}
