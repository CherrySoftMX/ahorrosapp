package com.cherrysoft.ahorrosapp.common.core.models.specs.piggybank;

import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdatePiggyBankSpec {
  private final String ownerUsername;
  private final String oldPbName;
  private final PiggyBank updatedPb;
}
