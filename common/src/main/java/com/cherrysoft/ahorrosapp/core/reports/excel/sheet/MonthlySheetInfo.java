package com.cherrysoft.ahorrosapp.core.reports.excel.sheet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.poi.ss.util.CellReference;

import static com.cherrysoft.ahorrosapp.core.reports.excel.sheet.SheetSavingsTableGenerator.SAVING_AMOUNT_COL;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class MonthlySheetInfo {
  private final int monthlyDailySavingsCount;

  public String getAmountCellsRange() {
    int firstRow = SheetSavingsTableGenerator.TABLE_INITIAL_ROW + 1;
    String amountColString = CellReference.convertNumToColString(SAVING_AMOUNT_COL);
    String startRange = String.format("%s%d", amountColString, firstRow + 1);
    String endRange = String.format("%s%d", amountColString, dailySavingsTableLastRow() + 1);
    return String.format("%s:%s", startRange, endRange);
  }

  public int dailySavingsTableLastRow() {
    return SheetSavingsTableGenerator.TABLE_INITIAL_ROW + monthlyDailySavingsCount;
  }

  private int sheetSummaryTableInitialRow() {
    return dailySavingsTableLastRow() + 2;
  }

  public int monthTotalAmountRow() {
    return sheetSummaryTableInitialRow();
  }

  public int monthAccumAmountRow() {
    return monthTotalAmountRow() + 1;
  }

  public int monthAverageAmountRow() {
    return monthAccumAmountRow() + 1;
  }

  public int startAmountRow() {
    return monthAverageAmountRow() + 1;
  }

  public String startAmountCellAddressString() {
    return CellReference.convertNumToColString(1) + (startAmountRow() + 1);
  }

}
