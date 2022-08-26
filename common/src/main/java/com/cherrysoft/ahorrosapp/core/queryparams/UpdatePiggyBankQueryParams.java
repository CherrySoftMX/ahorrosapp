package com.cherrysoft.ahorrosapp.core.queryparams;

import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdatePiggyBankQueryParams {
  private String ownerUsername;
  private String oldPbName;
  private PiggyBank updatedPb;
}
