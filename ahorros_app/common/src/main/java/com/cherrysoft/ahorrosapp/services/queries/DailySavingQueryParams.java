package com.cherrysoft.ahorrosapp.services.queries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

import static com.cherrysoft.ahorrosapp.utils.DateUtils.today;

@AllArgsConstructor
@Getter
@ToString
public class DailySavingQueryParams {
  private String ownerUsername;
  private String pbName;
  private LocalDate date;

  public DailySavingQueryParams(String ownerUsername, String pbName) {
    this(ownerUsername, pbName, today());
  }

}
