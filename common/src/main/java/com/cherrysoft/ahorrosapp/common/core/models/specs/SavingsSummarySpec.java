package com.cherrysoft.ahorrosapp.common.core.models.specs;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class SavingsSummarySpec {
  private final String ownerUsername;
  private final String pbName;
  private final Map<String, String> summaryOptions;

  public SavingsSummarySpec(String ownerUsername, String pbName) {
    this(ownerUsername, pbName, new HashMap<>());
  }

  public String getMonth() {
    return summaryOptions.get("month");
  }

  public String getStartMonth() {
    return summaryOptions.get("start");
  }

  public String getEndMonth() {
    return summaryOptions.get("end");
  }

}
