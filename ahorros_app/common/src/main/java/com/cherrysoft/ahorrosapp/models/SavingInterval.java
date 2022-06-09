package com.cherrysoft.ahorrosapp.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class SavingInterval {
  @Column
  public LocalDate startDate;

  @Column
  public LocalDate endDate;
}
