package com.cherrysoft.ahorrosapp.core.reports.excel;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.splitters.SavingsSplit;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

@RequiredArgsConstructor
public class MonthlySheetGenerator {
  public static final int TABLE_INITIAL_ROW = 2;
  private final Workbook workbook;
  @Setter
  private SavingsSplit savingsSplit;
  private Sheet sheet;

  public void createSheet(SavingsSplit split) {
    setSavingsSplit(split);
    sheet = workbook.createSheet(savingsSplit.splitRepresentation());
    createSheetHeader();
    createSavingsTableHeader();
    populateSavingsTable();
    createSheetSummary();
  }

  private void createSheetHeader() {
    Font sheetHeaderFont = workbook.createFont();
    sheetHeaderFont.setBold(true);
    sheetHeaderFont.setFontHeightInPoints((short) 18);

    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.cloneStyleFrom(centeredCellStyle());
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setFont(sheetHeaderFont);

    Row sheetHeader = sheet.createRow(0);
    sheetHeader.createCell(0);
    sheetHeader.setHeight((short) -1);

    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
    Cell cell = sheet.getRow(0).getCell(0);
    cell.setCellValue(savingsSplit.splitRepresentation());
    cell.setCellStyle(cellStyle);
  }

  private void createSavingsTableHeader() {
    Row tableHeader = sheet.createRow(2);

    Cell amountCol = tableHeader.createCell(1);
    amountCol.setCellValue("AMOUNT");
    amountCol.setCellStyle(centeredCellStyle());

    Cell accumulatedCol = tableHeader.createCell(2);
    accumulatedCol.setCellValue("ACCUMULATED");
    accumulatedCol.setCellStyle(centeredCellStyle());
  }

  private void populateSavingsTable() {
    List<DailySaving> dailySavings = savingsSplit.getDailySavings();
    int tableInitialRow = TABLE_INITIAL_ROW;
    for (DailySaving dailySaving : dailySavings) {
      Row row = sheet.createRow(tableInitialRow);

      Cell dateCell = row.createCell(0);
      dateCell.setCellValue(dailySaving.getDate());
      dateCell.setCellStyle(dateCellStyle());

      Cell amountCell = row.createCell(1);
      amountCell.setCellValue(dailySaving.getAmount().toString());
      amountCell.setCellStyle(currencyCellStyle());

      tableInitialRow++;
    }
  }

  private void createSheetSummary() {
    int tableInitialRow = sheetSummaryTableInitialRow();
    System.out.println(tableInitialRow);
  }

  private CellStyle centeredCellStyle() {
    CellStyle centeredCellStyle = workbook.createCellStyle();
    centeredCellStyle.setAlignment(HorizontalAlignment.CENTER);
    return centeredCellStyle;
  }

  private CellStyle currencyCellStyle() {
    CellStyle currencyCellStyle = workbook.createCellStyle();
    currencyCellStyle.setDataFormat((short) 8);
    return currencyCellStyle;
  }

  private CellStyle dateCellStyle() {
    CellStyle dateCellStyle = workbook.createCellStyle();
    CreationHelper creationHelper = workbook.getCreationHelper();
    short dateFormat = creationHelper.createDataFormat().getFormat("MM/dd/yyyy");
    dateCellStyle.setDataFormat(dateFormat);
    return dateCellStyle;
  }

  private int sheetSummaryTableInitialRow() {
    return TABLE_INITIAL_ROW + savingsSplit.getDailySavings().size() + 1;
  }

}
