package com.cherrysoft.ahorrosapp.core.reports.excel.sheet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import static com.cherrysoft.ahorrosapp.core.reports.excel.ExcelWorkbookUtils.currencyCellStyle;

public class SheetSummaryTableGenerator extends SheetComponentGenerator {
  public SheetSummaryTableGenerator(Sheet sheet) {
    super(sheet);
  }

  @Override
  public void generateComponent() {
    createMonthTotalAmountRow();
  }

  private void createMonthTotalAmountRow() {
    Row row = sheet().createRow(sheetInfo().monthTotalAmountRow());

    Cell monthTotalCellLabel = row.createCell(0);
    monthTotalCellLabel.setCellValue("MONTH TOTAL:");

    Cell monthTotalCell = row.createCell(1);
    monthTotalCell.setCellFormula(String.format("SUM(%s)", sheetInfo().getAmountCellsRange()));
    monthTotalCell.setCellStyle(currencyCellStyle(sheet().getWorkbook()));
  }

}
