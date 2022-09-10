package com.cherrysoft.ahorrosapp.core.reports.excel.sheet;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

import static com.cherrysoft.ahorrosapp.core.reports.excel.ExcelWorkbookUtils.*;
import static java.util.Objects.nonNull;

public class SheetSavingsTableGenerator extends SheetComponentGenerator {
  public static final int TABLE_INITIAL_ROW = 2;
  public static final int SAVING_DATE_COL = 0;
  public static final int SAVING_AMOUNT_COL = 1;
  public static final int SAVINGS_ACCUM_COL = 2;
  public static final int SAVING_DESCRIPTION_COL = 3;
  private final List<DailySaving> dailySavings;
  private Row previousRow;
  private Row currentRow;
  private DailySaving currentDailySaving;

  public SheetSavingsTableGenerator(Sheet sheet, List<DailySaving> dailySavings) {
    super(sheet);
    this.dailySavings = dailySavings;
  }

  @Override
  public void generateComponent() {
    createSavingsTableHeader();
    createSavingsTable();
  }

  private void createSavingsTableHeader() {
    Row tableHeader = sheet().createRow(TABLE_INITIAL_ROW);

    Cell amountCol = tableHeader.createCell(SAVING_AMOUNT_COL);
    amountCol.setCellValue("AMOUNT");
    amountCol.setCellStyle(centeredCellStyle(workbook()));

    Cell accumulatedCol = tableHeader.createCell(SAVINGS_ACCUM_COL);
    accumulatedCol.setCellValue("ACCUMULATED");
    accumulatedCol.setCellStyle(centeredCellStyle(workbook()));

    Cell descriptionCol = tableHeader.createCell(SAVING_DESCRIPTION_COL);
    descriptionCol.setCellValue("DESCRIPTION");
    descriptionCol.setCellStyle(centeredCellStyle(workbook()));
  }

  private void createSavingsTable() {
    int tableSavingsInitialRow = TABLE_INITIAL_ROW + 1;
    previousRow = null;
    for (DailySaving dailySaving : dailySavings) {
      currentRow = sheet().createRow(tableSavingsInitialRow);
      currentDailySaving = dailySaving;

      generateDateCell();
      generateAmountCell();
      generateAccumCell();
      generateDescriptionCell();

      tableSavingsInitialRow++;
      previousRow = currentRow;
    }
  }

  private void generateDateCell() {
    Cell dateCell = currentRow.createCell(SAVING_DATE_COL);
    dateCell.setCellValue(currentDailySaving.getDate());
    dateCell.setCellStyle(dateCellStyle(workbook()));
  }

  private void generateAmountCell() {
    Cell amountCell = currentRow.createCell(SAVING_AMOUNT_COL);
    amountCell.setCellValue(currentDailySaving.getAmount().doubleValue());
    amountCell.setCellStyle(currencyCellStyle(workbook()));
  }

  private void generateAccumCell() {
    Cell accumCell = currentRow.createCell(SAVINGS_ACCUM_COL);
    accumCell.setCellStyle(currencyCellStyle(workbook()));

    if (nonNull(previousRow)) {
      accumCell.setCellFormula(
          String.format("SUM(%s, %s)",
              getCellAddressAsString(currentRow.getCell(SAVING_AMOUNT_COL)),
              getCellAddressAsString(previousRow.getCell(SAVINGS_ACCUM_COL))
          )
      );
    }
  }

  private void generateDescriptionCell() {
    Cell descriptionCell = currentRow.createCell(SAVING_DESCRIPTION_COL);
    descriptionCell.setCellValue(currentDailySaving.getDescription());
  }

}
