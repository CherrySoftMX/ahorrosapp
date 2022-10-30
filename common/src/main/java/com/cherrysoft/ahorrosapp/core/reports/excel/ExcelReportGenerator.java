package com.cherrysoft.ahorrosapp.core.reports.excel;

import com.cherrysoft.ahorrosapp.core.reports.excel.sheet.MonthlySheetGenerator;
import com.cherrysoft.ahorrosapp.core.collectors.SavingsGroup;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class ExcelReportGenerator {
  private final List<SavingsGroup> savingsGroups;
  private Workbook workbook;

  public byte[] generateReport() {
    try {
      return tryToGenerateReport();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private byte[] tryToGenerateReport() throws IOException {
    var byteArray = new ByteArrayOutputStream();
    workbook = new SXSSFWorkbook(1000);
    generateSheetPerMonth();
    evaluateAllFormulas();
    workbook.write(byteArray);
    workbook.close();
    return byteArray.toByteArray();
  }

  private void generateSheetPerMonth() {
    MonthlySheetGenerator previousMonthlySheet = null;
    for (SavingsGroup savingsGroup : savingsGroups) {
      MonthlySheetGenerator monthlySheet = new MonthlySheetGenerator(workbook);
      if (nonNull(previousMonthlySheet)) {
        monthlySheet.setPrevSheetContext(previousMonthlySheet.getMonthlyContext());
      }
      monthlySheet.createSheet(savingsGroup);
      previousMonthlySheet = monthlySheet;
    }
  }

  private void evaluateAllFormulas() {
    workbook.setForceFormulaRecalculation(true);
    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
    evaluator.evaluateAll();
  }

}
