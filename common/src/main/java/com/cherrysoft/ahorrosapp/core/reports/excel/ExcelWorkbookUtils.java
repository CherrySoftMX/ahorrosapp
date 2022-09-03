package com.cherrysoft.ahorrosapp.core.reports.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelWorkbookUtils {

  public static CellStyle dateCellStyle(Workbook workbook) {
    CellStyle dateCellStyle = workbook.createCellStyle();
    CreationHelper creationHelper = workbook.getCreationHelper();
    short dateFormat = creationHelper.createDataFormat().getFormat("MM/dd/yyyy");
    dateCellStyle.setDataFormat(dateFormat);
    return dateCellStyle;
  }

  public static CellStyle currencyCellStyle(Workbook workbook) {
    CellStyle currencyCellStyle = workbook.createCellStyle();
    currencyCellStyle.setDataFormat((short) 8);
    return currencyCellStyle;
  }

  public static CellStyle centeredCellStyle(Workbook workbook) {
    CellStyle centeredCellStyle = workbook.createCellStyle();
    centeredCellStyle.setAlignment(HorizontalAlignment.CENTER);
    return centeredCellStyle;
  }

}
