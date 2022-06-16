package com.cherrysoft.ahorrosapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

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

}
