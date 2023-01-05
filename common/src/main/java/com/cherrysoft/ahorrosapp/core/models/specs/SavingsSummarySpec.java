package com.cherrysoft.ahorrosapp.core.models.specs;

import com.cherrysoft.ahorrosapp.core.SavingSummaryType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Data
@RequiredArgsConstructor
public class SavingsSummarySpec {
  private final String ownerUsername;
  private final String pbName;
  private final Map<String, String> summaryOptions;

  public SavingsSummarySpec(String ownerUsername, String pbName) {
    this(ownerUsername, pbName, new HashMap<>());
  }

  public SavingSummaryType getSummaryType() {
    String type = summaryOptions.get("type");
    if (isNull(type)) {
      throw new IllegalArgumentException("A summary type must be specified!");
    }
    return SavingSummaryType.of(type);
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
