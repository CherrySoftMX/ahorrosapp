package com.cherrysoft.ahorrosapp.core.reports.excel.sheet;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import java.util.List;

import static com.cherrysoft.ahorrosapp.core.reports.excel.ExcelWorkbookUtils.*;

@RequiredArgsConstructor
public class SheetSavingsTableGenerator {
  public static final int SAVING_DATE_COL = 0;
  public static final int SAVING_AMOUNT_COL = 1;
  public static final int SAVINGS_ACCUM_COL = 2;
  public static final int SAVING_DESCRIPTION_COL = 3;
  private final Sheet sheet;
  private final List<DailySaving> dailySavings;
  private int initialRow;

  public void createTableOnRow(int initialRow) {
    this.initialRow = initialRow;
    createSavingsTableHeader();
    createSavingsTable();
  }

  private void createSavingsTableHeader() {
    Row tableHeader = sheet.createRow(initialRow);

    Cell amountCol = tableHeader.createCell(SAVING_AMOUNT_COL);
    amountCol.setCellValue("AMOUNT");
    amountCol.setCellStyle(centeredCellStyle(sheet.getWorkbook()));

    Cell accumulatedCol = tableHeader.createCell(SAVINGS_ACCUM_COL);
    accumulatedCol.setCellValue("ACCUMULATED");
    accumulatedCol.setCellStyle(centeredCellStyle(sheet.getWorkbook()));

    Cell descriptionCol = tableHeader.createCell(SAVING_DESCRIPTION_COL);
    descriptionCol.setCellValue("DESCRIPTION");
    descriptionCol.setCellStyle(centeredCellStyle(sheet.getWorkbook()));
  }

  private void createSavingsTable() {
    int tableInitialRow = initialRow + 1;
    for (DailySaving dailySaving : dailySavings) {
      Row row = sheet.createRow(tableInitialRow);

      Cell dateCell = row.createCell(SAVING_DATE_COL);
      dateCell.setCellValue(dailySaving.getDate());
      dateCell.setCellStyle(dateCellStyle(sheet.getWorkbook()));

      Cell amountCell = row.createCell(SAVING_AMOUNT_COL);
      amountCell.setCellValue(dailySaving.getAmount().doubleValue());
      amountCell.setCellStyle(currencyCellStyle(sheet.getWorkbook()));

      Cell descriptionCell = row.createCell(SAVING_DESCRIPTION_COL);
      descriptionCell.setCellValue(dailySaving.getDescription());

      tableInitialRow++;
    }
  }

  public String getAmountCellsRange() {
    int firstRow = initialRow + 1;
    String amountColString = CellReference.convertNumToColString(SAVING_AMOUNT_COL);
    String startRange = String.format("%s%d", amountColString, firstRow + 1);
    String endRange = String.format("%s%d", amountColString, lastTableRow() + 1);
    return String.format("%s:%s", startRange, endRange);
  }

  public int lastTableRow() {
    return MonthlySheetGenerator.TABLE_INITIAL_ROW + dailySavings.size();
  }

}
