package com.cherrysoft.ahorrosapp.core.params;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

import static com.cherrysoft.ahorrosapp.utils.DateUtils.today;

@Getter
@ToString(callSuper = true)
public class DailySavingParams extends SavingParams {
  private final LocalDate date;

  public DailySavingParams(String ownerUsername, String pbName) {
    super(ownerUsername, pbName);
    this.date = today();
  }

  public DailySavingParams(String ownerUsername, String pbName, LocalDate date) {
    super(ownerUsername, pbName);
    this.date = date;
  }

}
