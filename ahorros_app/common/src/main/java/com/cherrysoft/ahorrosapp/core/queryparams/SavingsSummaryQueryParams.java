package com.cherrysoft.ahorrosapp.core.queryparams;

import lombok.ToString;

import java.util.Map;

@ToString(callSuper = true)
public class SavingsSummaryQueryParams extends SavingQueryParams {
  private final Map<String, String> summaryOptions;

  public SavingsSummaryQueryParams(String ownerUsername, String pbName, Map<String, String> summaryOptions) {
    super(ownerUsername, pbName);
    this.summaryOptions = summaryOptions;
  }

}
