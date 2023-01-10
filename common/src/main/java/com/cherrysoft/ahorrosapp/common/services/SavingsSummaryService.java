package com.cherrysoft.ahorrosapp.common.services;

import com.cherrysoft.ahorrosapp.common.core.IntervalSavingsGapFiller;
import com.cherrysoft.ahorrosapp.common.core.IntervalSavingsSummary;
import com.cherrysoft.ahorrosapp.common.core.PiggyBankSummary;
import com.cherrysoft.ahorrosapp.common.core.collectors.MonthlySavingsCollector;
import com.cherrysoft.ahorrosapp.common.core.fetchers.SavingsFetcherStrategy;
import com.cherrysoft.ahorrosapp.common.core.fetchers.factories.SavingsFetcherStrategyFactory;
import com.cherrysoft.ahorrosapp.common.core.models.ExcelReportResult;
import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.core.models.specs.SavingsSummarySpec;
import com.cherrysoft.ahorrosapp.common.core.reports.excel.ExcelReportGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SavingsSummaryService {
  private final SavingsFetcherStrategyFactory fetcherStrategyFactory;
  private final PiggyBankService pbService;

  public IntervalSavingsSummary getMonthlySavingsSummary(SavingsSummarySpec spec) {
    var monthlyFetcher = fetcherStrategyFactory.createMonthlyFetcherStrategy(spec);
    return new IntervalSavingsSummary(monthlyFetcher.fetchSavings());
  }

  public ExcelReportResult getMonthlySavingsSummaryAsXlsx(SavingsSummarySpec spec) {
    var fetcher = fetcherStrategyFactory.createMonthlyFetcherStrategy(spec);
    return generateExcelReport(fetcher);
  }

  public IntervalSavingsSummary getIntervalSavingsSummary(SavingsSummarySpec spec) {
    var intervalMonthFetcher = fetcherStrategyFactory.createIntervalMonthFetcherStrategy(spec);
    return new IntervalSavingsSummary(intervalMonthFetcher.fetchSavings());
  }

  public ExcelReportResult getIntervalSavingsSummaryAsXlsx(SavingsSummarySpec spec) {
    var fetcher = fetcherStrategyFactory.createIntervalMonthFetcherStrategy(spec);
    return generateExcelReport(fetcher);
  }

  private ExcelReportResult generateExcelReport(SavingsFetcherStrategy fetcher) {
    var gapFiller = new IntervalSavingsGapFiller(fetcher.fetchSavings(), fetcher.startDay(), fetcher.endDay());
    var monthlySavingsCollector = new MonthlySavingsCollector(gapFiller.fillGaps());
    return new ExcelReportGenerator(monthlySavingsCollector.collect()).generateReport();
  }

  public PiggyBankSummary getPiggyBankSummary(SavingsSummarySpec spec) {
    PiggyBank pb = pbService.getPiggyBankByName(spec.getOwnerUsername(), spec.getPbName());
    return pb.getPiggyBankSummary();
  }

}
