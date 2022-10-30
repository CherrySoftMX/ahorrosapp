package com.cherrysoft.ahorrosapp.core.reports.excel.sheet;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import static com.cherrysoft.ahorrosapp.core.reports.excel.ExcelWorkbookUtils.*;
import static java.util.Objects.isNull;

public class SheetSavingsTableGenerator extends SheetComponentGenerator {
  public static final int TABLE_INITIAL_ROW = 2;
  public static final int SAVING_DATE_COL = 0;
  public static final int SAVING_AMOUNT_COL = 1;
  public static final int SAVINGS_ACCUM_COL = 2;
  public static final int SAVING_DESCRIPTION_COL = 3;
  private Row prevRow;
  private Row currentRow;
  private DailySaving currentSaving;

  public SheetSavingsTableGenerator(
      MonthlySheetContext sheetContext,
      MonthlySheetContext prevSheetContext
  ) {
    super(sheetContext, prevSheetContext);
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
    prevRow = null;
    for (DailySaving savings : sheetContext().getSavings()) {
      currentRow = sheet().createRow(tableSavingsInitialRow);
      currentSaving = savings;

      generateDateCell();
      generateAmountCell();
      generateAccumCell();
      generateDescriptionCell();

      tableSavingsInitialRow++;
      prevRow = currentRow;
    }
  }

  private void generateDateCell() {
    Cell dateCell = currentRow.createCell(SAVING_DATE_COL);
    dateCell.setCellValue(currentSaving.getDate());
    dateCell.setCellStyle(dateCellStyle(workbook()));
  }

  private void generateAmountCell() {
    Cell amountCell = currentRow.createCell(SAVING_AMOUNT_COL);
    amountCell.setCellValue(currentSaving.getAmount().doubleValue());
    amountCell.setCellStyle(currencyCellStyle(workbook()));
  }

  private void generateAccumCell() {
    Cell accumCell = currentRow.createCell(SAVINGS_ACCUM_COL);
    accumCell.setCellStyle(currencyCellStyle(workbook()));
    accumCell.setCellFormula(getAccumCellFormula());
  }

  private String getAccumCellFormula() {
    if (isFirstSheet() && isNull(prevRow)) {
      return String.format("%s",
          getCellAddressAsString(currentRow.getCell(SAVING_AMOUNT_COL))
      );
    }

    if (isNull(prevRow)) {
      int prevAccumAmountRowIndex = prevSheetContext().getAccumAmountRow();
      Row prevAccumAmountRow = prevSheetContext().getSheet().getRow(prevAccumAmountRowIndex);
      return String.format(
          "SUM('%s'!%s, %s)",
          prevSheetContext().getName(),
          getCellAddressAsString(prevAccumAmountRow.getCell(1)),
          getCellAddressAsString(currentRow.getCell(SAVING_AMOUNT_COL))
      );
    }

    return String.format("SUM(%s, %s)",
        getCellAddressAsString(currentRow.getCell(SAVING_AMOUNT_COL)),
        getCellAddressAsString(prevRow.getCell(SAVINGS_ACCUM_COL))
    );
  }

  private void generateDescriptionCell() {
    Cell descriptionCell = currentRow.createCell(SAVING_DESCRIPTION_COL);
    descriptionCell.setCellValue(currentSaving.getDescription());
  }

}
