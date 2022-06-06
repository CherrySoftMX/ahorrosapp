package com.cherrysoft.ahorrosapp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class User {
  @Id
  @GeneratedValue
  private Long id;

  private String username;
}
