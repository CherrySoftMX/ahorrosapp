package com.cherrysoft.ahorrosapp.core.reports.excel.sheet;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class MonthlySheetContext {
  private final Sheet sheet;
  private final String name;
  private final List<DailySaving> savings;

  public String getAmountColumnCellsRangeAddress() {
    int firstRow = SheetSavingsTableGenerator.TABLE_INITIAL_ROW + 1;
    String amountColString = CellReference.convertNumToColString(SheetSavingsTableGenerator.SAVING_AMOUNT_COL);
    String startRange = String.format("%s%d", amountColString, firstRow + 1);
    String endRange = String.format("%s%d", amountColString, getDailySavingsTableLastRow() + 1);
    return String.format("%s:%s", startRange, endRange);
  }

  public int getDailySavingsTableLastRow() {
    return SheetSavingsTableGenerator.TABLE_INITIAL_ROW + getSavingsCount();
  }

  private int getSheetSummaryTableInitialRow() {
    return getDailySavingsTableLastRow() + 2;
  }

  public int getTotalAmountRow() {
    return getSheetSummaryTableInitialRow();
  }

  public int getAccumAmountRow() {
    return getTotalAmountRow() + 1;
  }

  public int getAverageAmountRow() {
    return getAccumAmountRow() + 1;
  }

  public int getSavingsStartAmountRow() {
    return getAverageAmountRow() + 1;
  }

  public String getSavingsStartAmountCellAddress() {
    return CellReference.convertNumToColString(1) + (getSavingsStartAmountRow() + 1);
  }

  public String getLastAccumCellAddress() {
    return CellReference.convertNumToColString(SheetSavingsTableGenerator.SAVINGS_ACCUM_COL) + (getDailySavingsTableLastRow() + 1);
  }

  public int getSavingsCount() {
    return savings.size();
  }

}
