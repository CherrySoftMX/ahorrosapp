package com.cherrysoft.ahorrosapp.core.reports.excel.sheet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import static java.util.Objects.isNull;

@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor
public abstract class SheetComponentGenerator {
  private final MonthlySheetContext sheetContext;
  private final MonthlySheetContext prevSheetContext;

  public abstract void generateComponent();

  protected boolean isFirstSheet() {
    return isNull(prevSheetContext);
  }

  protected Workbook workbook() {
    return sheet().getWorkbook();
  }

  protected Sheet sheet() {
    return sheetContext.getSheet();
  }

}
