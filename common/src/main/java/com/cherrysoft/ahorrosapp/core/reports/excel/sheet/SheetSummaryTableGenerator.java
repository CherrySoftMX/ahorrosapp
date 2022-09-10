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
    createMonthAccumAmountRow();
    createMonthAverageAmountRow();
  }

  private void createMonthTotalAmountRow() {
    Row row = sheet().createRow(sheetContext().getMonthTotalAmountRow());

    Cell monthTotalAmountLabel = row.createCell(0);
    monthTotalAmountLabel.setCellValue("MONTH TOTAL:");

    Cell monthTotalAmount = row.createCell(1);
    monthTotalAmount.setCellFormula(String.format("SUM(%s)", sheetContext().getAmountCellsRangeAddressAsString()));
    monthTotalAmount.setCellStyle(currencyCellStyle(workbook()));
  }

  private void createMonthAccumAmountRow() {
    Row row = sheet().createRow(sheetContext().getMonthAccumAmountRow());

    Cell monthAccumAmountLabel = row.createCell(0);
    monthAccumAmountLabel.setCellValue("MONTH ACCUMULATED:");

    Cell monthAccumAmount = row.createCell(1);
    monthAccumAmount.setCellFormula(String.format("%s", sheetContext().getLastAccumCellAddressAsString()));
    monthAccumAmount.setCellStyle(currencyCellStyle(workbook()));
  }

  private void createMonthAverageAmountRow() {
    Row row = sheet().createRow(sheetContext().getMonthAverageAmountRow());
    String formulaTemplate = "IF(COUNTIF(%1$s, \"> 0\") > 0, AVERAGEIFS(%1$s, %1$s, \"> 0\"), \"NO SAVINGS FOR THIS MONTH\")";

    Cell monthAverageAmountLabel = row.createCell(0);
    monthAverageAmountLabel.setCellValue("MONTH AVERAGE:");

    Cell monthAverageAmount = row.createCell(1);
    monthAverageAmount.setCellFormula(String.format(formulaTemplate, sheetContext().getAmountCellsRangeAddressAsString()));
    monthAverageAmount.setCellStyle(currencyCellStyle(workbook()));
  }

}
