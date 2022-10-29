package com.cherrysoft.ahorrosapp.services;

import com.cherrysoft.ahorrosapp.core.IntervalSavingsGapFiller;
import com.cherrysoft.ahorrosapp.core.IntervalSavingsSummary;
import com.cherrysoft.ahorrosapp.core.PiggyBankSummary;
import com.cherrysoft.ahorrosapp.core.fetchers.factories.SavingsFetcherStrategyFactory;
import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.params.SavingsSummaryParams;
import com.cherrysoft.ahorrosapp.core.reports.excel.ExcelReportGenerator;
import com.cherrysoft.ahorrosapp.core.collectors.MonthlySavingsCollector;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SavingsSummaryService {
  private final SavingsFetcherStrategyFactory fetcherStrategyFactory;
  private final PiggyBankService pbService;

  public IntervalSavingsSummary getIntervalSavingsSummary(SavingsSummaryParams params) {
    var fetcher = fetcherStrategyFactory.createFetcherStrategy(params);
    return new IntervalSavingsSummary(fetcher.fetchSavings());
  }

  public byte[] getSavingsSummaryAsXlsxFile(SavingsSummaryParams params) {
    var fetcher = fetcherStrategyFactory.createFetcherStrategy(params);
    var gapFiller = new IntervalSavingsGapFiller(fetcher.fetchSavings(), fetcher.startDay(), fetcher.endDay());
    var savingsSplitter = new MonthlySavingsCollector(gapFiller.fillGaps());
    return new ExcelReportGenerator(savingsSplitter.collect()).generateReport();
  }

  public PiggyBankSummary getPiggyBankSummary(SavingsSummaryParams params) {
    PiggyBank pb = pbService.getPiggyBank(params);
    return pb.getPiggyBankSummary();
  }

}
