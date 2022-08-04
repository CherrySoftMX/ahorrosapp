package com.cherrysoft.ahorrosapp.core.queryparams;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public abstract class SavingQueryParams {
  private final String ownerUsername;
  private final String pbName;
}
