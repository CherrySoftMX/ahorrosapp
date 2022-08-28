package com.cherrysoft.ahorrosapp.core.reports.excel;

import com.cherrysoft.ahorrosapp.core.splitters.SavingsSplit;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class ExcelReportGenerator {
  private final List<SavingsSplit> savingsSplits;
  private Workbook workbook;

  public byte[] generateReport() {
    var byteArray = new ByteArrayOutputStream();
    try {
      workbook = new SXSSFWorkbook(1000);
      generateSheets();
      workbook.write(byteArray);
      workbook.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return byteArray.toByteArray();
  }

  private void generateSheets() {
    MonthlySheetGenerator monthlySheetGenerator = new MonthlySheetGenerator(workbook);
    savingsSplits.forEach(monthlySheetGenerator::createSheet);
  }

}
