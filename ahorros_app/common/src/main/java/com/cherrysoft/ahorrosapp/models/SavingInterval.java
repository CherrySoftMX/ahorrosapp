package com.cherrysoft.ahorrosapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

import static com.cherrysoft.ahorrosapp.utils.DateUtils.today;
import static java.util.Objects.isNull;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class SavingInterval {
  @Column
  private LocalDate startDate;

  @Column
  private LocalDate endDate;

  public SavingInterval(LocalDate endDate) {
    this.endDate = endDate;
  }

  public void ensureSavingsIntervalIntegrity() {
    if (isNull(startDate) && endDate.isBefore(today())) {
      throw new RuntimeException("Invalid end date");
    }
    if (startDate.isAfter(endDate)) {
      throw new RuntimeException("Invalid start date");
    }
  }
}
