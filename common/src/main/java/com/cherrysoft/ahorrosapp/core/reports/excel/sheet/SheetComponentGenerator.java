package com.cherrysoft.ahorrosapp.core.reports.excel.sheet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor
public abstract class SheetComponentGenerator {
  private final Sheet sheet;
  private MonthlySheetInfo sheetInfo;
  private MonthlySheetInfo previousSheetInfo;

  public abstract void generateComponent();

  protected Workbook workbook() {
    return sheet.getWorkbook();
  }

}
