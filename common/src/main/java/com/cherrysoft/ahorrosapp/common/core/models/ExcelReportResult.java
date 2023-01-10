package com.cherrysoft.ahorrosapp.common.core.models;

import lombok.Data;

@Data
public class ExcelReportResult {
  private final String fileName;
  private final byte[] reportByteArray;
}
