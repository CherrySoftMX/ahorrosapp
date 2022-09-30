package com.cherrysoft.ahorrosapp.core.params;

import com.cherrysoft.ahorrosapp.core.SavingSummaryFormatType;
import com.cherrysoft.ahorrosapp.core.SavingSummaryType;
import lombok.ToString;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@ToString(callSuper = true)
public class SavingsSummaryParams extends SavingParams {
  private final Map<String, String> summaryOptions;

  public SavingsSummaryParams(String ownerUsername, String pbName) {
    this(ownerUsername, pbName, new HashMap<>());
  }

  public SavingsSummaryParams(String ownerUsername, String pbName, Map<String, String> summaryOptions) {
    super(ownerUsername, pbName);
    this.summaryOptions = summaryOptions;
  }

  public SavingSummaryType getSummaryType() {
    String type = summaryOptions.get("type");
    if (isNull(type)) {
      throw new IllegalArgumentException("A summary type must be specified!");
    }
    return Arrays.stream(SavingSummaryType.values())
        .filter(t -> t.toString().equals(type.toLowerCase()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid type: " + type));
  }

  public SavingSummaryFormatType getSummaryFormatType() {
    String formatType = summaryOptions.get("format");
    if (isNull(formatType)) {
      return SavingSummaryFormatType.JSON;
    }
    return Arrays.stream(SavingSummaryFormatType.values())
        .filter(format -> format.toString().equals(formatType.toLowerCase()))
        .findFirst()
        .orElse(SavingSummaryFormatType.JSON);
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
