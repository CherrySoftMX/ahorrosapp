package com.cherrysoft.ahorrosapp.core.reports.excel.sheet;

import com.cherrysoft.ahorrosapp.core.collectors.SavingsGroup;
import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

import static com.cherrysoft.ahorrosapp.core.reports.excel.ExcelWorkbookUtils.centeredCellStyle;

@RequiredArgsConstructor
public class MonthlySheetGenerator {
  private final Workbook workbook;
  private Sheet sheet;
  @Setter private SavingsGroup savingsGroup;
  @Setter private MonthlySheetContext previousSheetContext;

  public void createSheet(SavingsGroup group) {
    setSavingsGroup(group);
    sheet = workbook.createSheet(savingsGroup.getGroupName());
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
    cell.setCellValue(savingsGroup.getGroupName());
    cell.setCellStyle(cellStyle);
  }

  private void createSheetSavingsTable() {
    List<DailySaving> dailySavings = savingsGroup.getDailySavings();
    var savingsTableGenerator = new SheetSavingsTableGenerator(sheet, dailySavings);
    initSheetComponentContext(savingsTableGenerator);
    savingsTableGenerator.generateComponent();
  }

  private void createSheetSummary() {
    var summaryTableGenerator = new SheetSummaryTableGenerator(sheet);
    initSheetComponentContext(summaryTableGenerator);
    summaryTableGenerator.generateComponent();
  }

  private void initSheetComponentContext(SheetComponentGenerator componentGenerator) {
    componentGenerator.sheetContext(getMonthlyContext());
    componentGenerator.previousSheetContext(previousSheetContext);
  }

  public MonthlySheetContext getMonthlyContext() {
    return new MonthlySheetContext(savingsGroup.getDailySavings().size());
  }

}
