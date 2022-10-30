package com.cherrysoft.ahorrosapp.core.reports.excel.sheet;

import com.cherrysoft.ahorrosapp.core.collectors.SavingsGroup;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import static com.cherrysoft.ahorrosapp.core.reports.excel.ExcelWorkbookUtils.centeredCellStyle;

@RequiredArgsConstructor
public class MonthlySheetGenerator {
  private final Workbook workbook;
  private Sheet sheet;
  @Setter private SavingsGroup savingsGroup;
  @Setter private MonthlySheetContext prevSheetContext;

  public void createSheet(SavingsGroup group) {
    setSavingsGroup(group);
    sheet = workbook.createSheet(savingsGroup.getGroupName());
    createSheetHeader();
    generateSheetSavingsTable();
    generateSheetSummary();
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
    cell.setCellValue(savingsGroup.getGroupName());
    cell.setCellStyle(cellStyle);
  }

  private void generateSheetSavingsTable() {
    var savingsTableGenerator = new SheetSavingsTableGenerator(getMonthlyContext(), prevSheetContext);
    savingsTableGenerator.generateComponent();
  }

  private void generateSheetSummary() {
    var summaryTableGenerator = new SheetSummaryTableGenerator(getMonthlyContext(), prevSheetContext);
    summaryTableGenerator.generateComponent();
  }

  public MonthlySheetContext getMonthlyContext() {
    return MonthlySheetContext.builder()
        .sheet(sheet)
        .name(savingsGroup.getGroupName())
        .savings(savingsGroup.getSavings())
        .build();
  }

}
