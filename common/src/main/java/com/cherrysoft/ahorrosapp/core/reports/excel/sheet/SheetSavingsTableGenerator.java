package com.cherrysoft.ahorrosapp.core.reports.excel;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

import static com.cherrysoft.ahorrosapp.core.reports.excel.ExcelWorkbookUtils.*;

@RequiredArgsConstructor
public class MonthlySavingsTableGenerator {
  public static final int SAVING_DATE_COL = 0;
  public static final int SAVING_AMOUNT_COL = 1;
  public static final int SAVINGS_ACCUM_COL = 2;
  private final Workbook workbook;
  private final Sheet sheet;
  private final List<DailySaving> dailySavings;
  private int initialRow;

  public void generateTableOnRow(int initialRow) {
    this.initialRow = initialRow;
    createSavingsTableHeader();
    populateSavingsTable();
  }

  private void createSavingsTableHeader() {
    Row tableHeader = sheet.createRow(initialRow);

    Cell amountCol = tableHeader.createCell(SAVING_AMOUNT_COL);
    amountCol.setCellValue("AMOUNT");
    amountCol.setCellStyle(centeredCellStyle(workbook));

    Cell accumulatedCol = tableHeader.createCell(SAVINGS_ACCUM_COL);
    accumulatedCol.setCellValue("ACCUMULATED");
    accumulatedCol.setCellStyle(centeredCellStyle(workbook));
  }

  private void populateSavingsTable() {
    int tableInitialRow = initialRow + 1;
    for (DailySaving dailySaving : dailySavings) {
      Row row = sheet.createRow(tableInitialRow);

      Cell dateCell = row.createCell(SAVING_DATE_COL);
      dateCell.setCellValue(dailySaving.getDate());
      dateCell.setCellStyle(dateCellStyle(workbook));

      Cell amountCell = row.createCell(SAVING_AMOUNT_COL);
      amountCell.setCellValue(dailySaving.getAmount().toString());
      amountCell.setCellStyle(currencyCellStyle(workbook));

      tableInitialRow++;
    }
  }

  public String getAmountCellsRange() {
    int firstRow = initialRow + 1;
    String amountLetterCol = String.valueOf(toLetterCol(SAVING_AMOUNT_COL));
    String startRange = amountLetterCol + firstRow + 1;
    String endRange = amountLetterCol + getLastRow() + 1;
    return String.format("%s:%s", startRange, endRange);
  }

  public int getLastRow() {
    return MonthlySheetGenerator.TABLE_INITIAL_ROW + dailySavings.size();
  }

}
