package com.cherrysoft.ahorrosapp.core.reports.excel.sheet;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.splitters.SavingsSplit;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

import static com.cherrysoft.ahorrosapp.core.reports.excel.ExcelWorkbookUtils.centeredCellStyle;

@RequiredArgsConstructor
public class MonthlySheetGenerator {
  public static final int TABLE_INITIAL_ROW = 2;
  private final Workbook workbook;
  @Setter private SavingsSplit savingsSplit;
  private Sheet sheet;
  private SheetSavingsTableGenerator savingsTableGenerator;
  private SheetSummaryTableGenerator summaryTableGenerator;

  public void createSheet(SavingsSplit split) {
    setSavingsSplit(split);
    sheet = workbook.createSheet(savingsSplit.splitRepresentation());
    createSheetHeader();
    createSheetSavingsTable();
    createSheetSummary();
  }

  private void createSheetHeader() {
    Font sheetHeaderFont = workbook.createFont();
    sheetHeaderFont.setBold(true);
    sheetHeaderFont.setFontHeightInPoints((short) 18);

    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.cloneStyleFrom(centeredCellStyle(workbook));
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

  private void createSheetSavingsTable() {
    List<DailySaving> dailySavings = savingsSplit.getDailySavings();
    savingsTableGenerator = new SheetSavingsTableGenerator(sheet, dailySavings);
    savingsTableGenerator.createTableOnRow(TABLE_INITIAL_ROW);
  }

  private void createSheetSummary() {
    summaryTableGenerator = new SheetSummaryTableGenerator(sheet);
    summaryTableGenerator.createSummaryTableOnRow(
        sheetSummaryTableInitialRow(),
        savingsTableGenerator.getAmountCellsRange()
    );
  }

  private int sheetSummaryTableInitialRow() {
    return savingsTableGenerator.lastTableRow() + 2;
  }

}
