package com.cherrysoft.ahorrosapp.core.queryparams;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

import static com.cherrysoft.ahorrosapp.utils.DateUtils.today;

@Getter
@ToString(callSuper = true)
public class DailySavingQueryParams extends SavingQueryParams {
  private final LocalDate date;

  public DailySavingQueryParams(String ownerUsername, String pbName) {
    super(ownerUsername, pbName);
    this.date = today();
  }

  public DailySavingQueryParams(String ownerUsername, String pbName, LocalDate date) {
    super(ownerUsername, pbName);
    this.date = date;
  }

}
