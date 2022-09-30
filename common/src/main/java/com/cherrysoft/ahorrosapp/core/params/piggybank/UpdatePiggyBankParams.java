package com.cherrysoft.ahorrosapp.core.params.piggybank;

import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdatePiggyBankParams {
  private final String ownerUsername;
  private final String oldPbName;
  private final PiggyBank updatedPb;
}
