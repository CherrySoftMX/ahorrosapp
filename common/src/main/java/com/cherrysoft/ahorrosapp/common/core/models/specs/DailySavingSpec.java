package com.cherrysoft.ahorrosapp.common.core.models.specs;

import com.cherrysoft.ahorrosapp.common.utils.DateUtils;
import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import lombok.Data;

import java.time.LocalDate;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNullElse;

@Data
public class DailySavingSpec {
  private final String ownerUsername;
  private final String pbName;
  private LocalDate savingDate;
  private DailySaving dailySaving;

  public DailySavingSpec(String ownerUsername, String pbName, LocalDate savingDate) {
    this.ownerUsername = ownerUsername;
    this.pbName = pbName;
    this.savingDate = requireNonNullElse(savingDate, DateUtils.today());
  }

  public DailySavingSpec(String ownerUsername, String pbName, DailySaving dailySaving) {
    this.ownerUsername = ownerUsername;
    this.pbName = pbName;
    this.dailySaving = dailySaving;
    this.savingDate = dailySaving.getDate();
  }

  public LocalDate getSavingDate() {
    if (isNull(dailySaving)) {
      return savingDate;
    }
    return dailySaving.getDate();
  }

}
