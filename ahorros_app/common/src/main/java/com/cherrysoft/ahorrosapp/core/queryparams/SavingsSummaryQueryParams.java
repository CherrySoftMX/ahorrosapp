package com.cherrysoft.ahorrosapp.core.queryparams;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString(callSuper = true)
public class SavingsSummaryQueryParams extends SavingQueryParams {
  private final Map<String, String> summaryOptions;

  public SavingsSummaryQueryParams(String ownerUsername, String pbName) {
    this(ownerUsername, pbName, new HashMap<>());
  }

  public SavingsSummaryQueryParams(String ownerUsername, String pbName, Map<String, String> summaryOptions) {
    super(ownerUsername, pbName);
    this.summaryOptions = summaryOptions;
  }

  public String getSummaryType() {
    return summaryOptions.get("type");
  }

  public String getRawDate() {
    return summaryOptions.get("date");
  }

}
