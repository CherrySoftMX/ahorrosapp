package com.cherrysoft.ahorrosapp.common.services;

import com.cherrysoft.ahorrosapp.common.core.IntervalSavingsGapFiller;
import com.cherrysoft.ahorrosapp.common.core.IntervalSavingsSummary;
import com.cherrysoft.ahorrosapp.common.core.PiggyBankSummary;
import com.cherrysoft.ahorrosapp.common.core.collectors.MonthlySavingsCollector;
import com.cherrysoft.ahorrosapp.common.core.interval.DatesInterval;
import com.cherrysoft.ahorrosapp.common.core.interval.MonthInterval;
import com.cherrysoft.ahorrosapp.common.core.interval.MonthsInterval;
import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.ExcelReportResult;
import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.core.models.specs.SavingsSummarySpec;
import com.cherrysoft.ahorrosapp.common.core.models.specs.piggybank.GetPiggyBankSpec;
import com.cherrysoft.ahorrosapp.common.core.reports.excel.ExcelReportGenerator;
import com.cherrysoft.ahorrosapp.common.services.dailysaving.GetDailySavingUC;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavingsSummaryService {
  private final GetDailySavingUC getDailySavingUC;
  private final PiggyBankService pbService;

  public IntervalSavingsSummary getMonthlySavingsSummary(SavingsSummarySpec spec) {
    var monthlyInterval = new MonthInterval(spec.getMonth());
    return new IntervalSavingsSummary(getSavingsForInterval(spec.asGetPiggyBankSpec(), monthlyInterval));
  }

  public ExcelReportResult getMonthlySavingsSummaryAsXlsx(SavingsSummarySpec spec) {
    var monthlyInterval = new MonthInterval(spec.getMonth());
    return generateExcelReport(spec.asGetPiggyBankSpec(), monthlyInterval);
  }

  public IntervalSavingsSummary getIntervalSavingsSummary(SavingsSummarySpec spec) {
    var monthsInterval = new MonthsInterval(spec.getStartMonth(), spec.getEndMonth());
    return new IntervalSavingsSummary(getSavingsForInterval(spec.asGetPiggyBankSpec(), monthsInterval));
  }

  public ExcelReportResult getIntervalSavingsSummaryAsXlsx(SavingsSummarySpec spec) {
    var monthsInterval = new MonthsInterval(spec.getStartMonth(), spec.getEndMonth());
    return generateExcelReport(spec.asGetPiggyBankSpec(), monthsInterval);
  }

  private ExcelReportResult generateExcelReport(GetPiggyBankSpec getPiggyBankSpec, DatesInterval datesInterval) {
    List<DailySaving> dailySavings = getSavingsForInterval(getPiggyBankSpec, datesInterval);
    var gapFiller = new IntervalSavingsGapFiller(dailySavings, datesInterval.startDay(), datesInterval.endDay());
    var monthlySavingsCollector = new MonthlySavingsCollector(gapFiller.fillGaps());
    return new ExcelReportGenerator(monthlySavingsCollector.groupByMonth()).generateReport();
  }

  private List<DailySaving> getSavingsForInterval(GetPiggyBankSpec spec, DatesInterval datesInterval) {
    return getDailySavingUC.getSavingsForInterval(spec, datesInterval);
  }

  public PiggyBankSummary getPiggyBankSummary(SavingsSummarySpec spec) {
    PiggyBank pb = pbService.getPiggyBankByName(spec.getOwnerUsername(), spec.getPbName());
    return pb.getPiggyBankSummary();
  }

}
