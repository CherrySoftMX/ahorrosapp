package com.cherrysoft.ahorrosapp.common.services;

import com.cherrysoft.ahorrosapp.common.core.IntervalSavingsGapFiller;
import com.cherrysoft.ahorrosapp.common.core.IntervalSavingsSummary;
import com.cherrysoft.ahorrosapp.common.core.PiggyBankSummary;
import com.cherrysoft.ahorrosapp.common.core.collectors.MonthlySavingsCollector;
import com.cherrysoft.ahorrosapp.common.core.fetchers.factories.SavingsFetcherStrategyFactory;
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

  public IntervalSavingsSummary getIntervalSavingsSummary(SavingsSummarySpec params) {
    var fetcher = fetcherStrategyFactory.createFetcherStrategy(params);
    return new IntervalSavingsSummary(fetcher.fetchSavings());
  }

  public byte[] getIntervalSavingsSummaryAsXlsx(SavingsSummarySpec params) {
    var fetcher = fetcherStrategyFactory.createFetcherStrategy(params);
    var gapFiller = new IntervalSavingsGapFiller(fetcher.fetchSavings(), fetcher.startDay(), fetcher.endDay());
    var monthlySavingsCollector = new MonthlySavingsCollector(gapFiller.fillGaps());
    return new ExcelReportGenerator(monthlySavingsCollector.collect()).generateReport();
  }

  public PiggyBankSummary getPiggyBankSummary(SavingsSummarySpec params) {
    PiggyBank pb = pbService.getPiggyBankByName(params.getOwnerUsername(), params.getPbName());
    return pb.getPiggyBankSummary();
  }

}
