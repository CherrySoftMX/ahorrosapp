package com.cherrysoft.ahorrosapp.core;

public enum SavingSummaryFormatType {
  JSON,
  XML,
  EXCEL;

  @Override
  public String toString() {
    return super.toString().toLowerCase();
  }

}
