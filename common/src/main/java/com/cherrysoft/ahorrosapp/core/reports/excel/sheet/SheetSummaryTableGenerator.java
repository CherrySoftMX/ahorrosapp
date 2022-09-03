package com.cherrysoft.ahorrosapp.core.reports.excel.sheet;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;

import static com.cherrysoft.ahorrosapp.core.reports.excel.ExcelWorkbookUtils.currencyCellStyle;

@RequiredArgsConstructor
public class SheetSummaryTableGenerator {
  private final Sheet sheet;
  private int initialRow;
  private String amountCellsRange;

  public void createSummaryTableOnRow(int initialRow, String amountCellsRange) {
    this.initialRow = initialRow;
    this.amountCellsRange = amountCellsRange;
    createMonthTotalAmountRow();
  }

  private void createMonthTotalAmountRow() {
    Row row = sheet.createRow(initialRow);

    Cell monthTotalCellLabel = row.createCell(0);
    monthTotalCellLabel.setCellValue("Month total: ");

    Workbook workbook = sheet.getWorkbook();
    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
    Cell monthTotalCell = row.createCell(1);
    monthTotalCell.setCellFormula(String.format("SUM(%s)", amountCellsRange));
    monthTotalCell.setCellStyle(currencyCellStyle(workbook));
    evaluator.evaluateFormulaCell(monthTotalCell);
  }

}
