package com.cherrysoft.ahorrosapp.common.core;

public enum SavingSummaryFormatType {
  JSON,
  XML,
  EXCEL;

  @Override
  public String toString() {
    return super.toString().toLowerCase();
  }

}
