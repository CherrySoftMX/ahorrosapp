package com.cherrysoft.ahorrosapp.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class SavingInterval {
  @Column
  private LocalDate startDate;

  @Column
  private LocalDate endDate;
}
