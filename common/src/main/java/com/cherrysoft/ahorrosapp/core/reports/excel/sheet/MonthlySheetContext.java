package com.cherrysoft.ahorrosapp.core.reports.excel.sheet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.util.CellReference;

import static com.cherrysoft.ahorrosapp.core.reports.excel.sheet.SheetSavingsTableGenerator.SAVINGS_ACCUM_COL;
import static com.cherrysoft.ahorrosapp.core.reports.excel.sheet.SheetSavingsTableGenerator.SAVING_AMOUNT_COL;

@Getter
@RequiredArgsConstructor
public class MonthlySheetContext {
  private final int monthlyDailySavingsCount;

  public String getAmountCellsRangeAddressAsString() {
    int firstRow = SheetSavingsTableGenerator.TABLE_INITIAL_ROW + 1;
    String amountColString = CellReference.convertNumToColString(SAVING_AMOUNT_COL);
    String startRange = String.format("%s%d", amountColString, firstRow + 1);
    String endRange = String.format("%s%d", amountColString, getDailySavingsTableLastRow() + 1);
    return String.format("%s:%s", startRange, endRange);
  }

  public int getDailySavingsTableLastRow() {
    return SheetSavingsTableGenerator.TABLE_INITIAL_ROW + monthlyDailySavingsCount;
  }

  private int getSheetSummaryTableInitialRow() {
    return getDailySavingsTableLastRow() + 2;
  }

  public int getMonthTotalAmountRow() {
    return getSheetSummaryTableInitialRow();
  }

  public int getMonthAccumAmountRow() {
    return getMonthTotalAmountRow() + 1;
  }

  public int getMonthAverageAmountRow() {
    return getMonthAccumAmountRow() + 1;
  }

  public int getSavingsStartAmountRow() {
    return getMonthAverageAmountRow() + 1;
  }

  public String getSavingsStartAmountCellAddressAsString() {
    return CellReference.convertNumToColString(1) + (getSavingsStartAmountRow() + 1);
  }

  public String getLastAccumCellAddressAsString() {
    return CellReference.convertNumToColString(SAVINGS_ACCUM_COL) + (getDailySavingsTableLastRow() + 1);
  }

}
