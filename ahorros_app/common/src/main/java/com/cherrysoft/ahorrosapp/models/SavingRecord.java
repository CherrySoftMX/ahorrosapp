package com.cherrysoft.ahorrosapp.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table
public class SavingRecord {
  @Id
  @GeneratedValue
  @Column(name = "saving_record_id")
  private Long id;

  @Column
  @NotNull
  private LocalDate date;

  @Column
  @NotNull
  private BigDecimal amount;

  @Column
  private String description;
}
