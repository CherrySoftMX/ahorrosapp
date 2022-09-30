package com.cherrysoft.ahorrosapp.core.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public abstract class SavingParams {
  private final String ownerUsername;
  private final String pbName;
}
