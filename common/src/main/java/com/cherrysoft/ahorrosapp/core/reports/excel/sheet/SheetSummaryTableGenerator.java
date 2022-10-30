package com.cherrysoft.ahorrosapp.core.reports.excel.sheet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import static com.cherrysoft.ahorrosapp.core.reports.excel.ExcelWorkbookUtils.currencyCellStyle;

public class SheetSummaryTableGenerator extends SheetComponentGenerator {

  public SheetSummaryTableGenerator(
      MonthlySheetContext sheetContext,
      MonthlySheetContext prevSheetContext
  ) {
    super(sheetContext, prevSheetContext);
  }

  @Override
  public void generateComponent() {
    createMonthTotalAmountRow();
    createMonthAccumAmountRow();
    createMonthAverageAmountRow();
  }

  private void createMonthTotalAmountRow() {
    Row row = sheet().createRow(sheetContext().getTotalAmountRow());

    Cell monthTotalAmountLabel = row.createCell(0);
    monthTotalAmountLabel.setCellValue("MONTH TOTAL:");

    Cell monthTotalAmount = row.createCell(1);
    monthTotalAmount.setCellFormula(String.format("SUM(%s)", sheetContext().getAmountColumnCellsRangeAddress()));
    monthTotalAmount.setCellStyle(currencyCellStyle(workbook()));
  }

  private void createMonthAccumAmountRow() {
    Row row = sheet().createRow(sheetContext().getAccumAmountRow());

    Cell monthAccumAmountLabel = row.createCell(0);
    monthAccumAmountLabel.setCellValue("MONTH ACCUMULATED:");

    Cell monthAccumAmount = row.createCell(1);
    monthAccumAmount.setCellFormula(String.format("%s", sheetContext().getLastAccumCellAddress()));
    monthAccumAmount.setCellStyle(currencyCellStyle(workbook()));
  }

  private void createMonthAverageAmountRow() {
    Row row = sheet().createRow(sheetContext().getAverageAmountRow());
    String formulaTemplate = "IF(COUNTIF(%1$s, \"> 0\") > 0, AVERAGEIFS(%1$s, %1$s, \"> 0\"), \"NO SAVINGS FOR THIS MONTH\")";

    Cell monthAverageAmountLabel = row.createCell(0);
    monthAverageAmountLabel.setCellValue("MONTH AVERAGE:");

    Cell monthAverageAmount = row.createCell(1);
    monthAverageAmount.setCellFormula(String.format(formulaTemplate, sheetContext().getAmountColumnCellsRangeAddress()));
    monthAverageAmount.setCellStyle(currencyCellStyle(workbook()));
  }

}
