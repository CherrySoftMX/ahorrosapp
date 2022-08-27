package com.cherrysoft.ahorrosapp.controllers.summaries.factories.imp;

import com.cherrysoft.ahorrosapp.controllers.summaries.factories.SummaryResponseFormatFactory;
import com.cherrysoft.ahorrosapp.controllers.summaries.reponsestrategies.ExcelSummaryResponseStrategy;
import com.cherrysoft.ahorrosapp.controllers.summaries.reponsestrategies.JsonSummaryResponseStrategy;
import com.cherrysoft.ahorrosapp.controllers.summaries.reponsestrategies.SummaryResponseStrategy;
import com.cherrysoft.ahorrosapp.core.SavingSummaryFormatType;
import com.cherrysoft.ahorrosapp.core.queryparams.SavingsSummaryQueryParams;
import com.cherrysoft.ahorrosapp.mappers.SavingsSummaryMapper;
import com.cherrysoft.ahorrosapp.services.SavingsSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class SummaryResponseFormatFactoryImp implements SummaryResponseFormatFactory {
  private final AutowireCapableBeanFactory beanFactory;

  @Override
  public SummaryResponseStrategy createSummaryResponseStrategy(SavingsSummaryQueryParams params) {
    SavingSummaryFormatType formatType = params.getSummaryFormatType();
    var summaryService = beanFactory.getBean(SavingsSummaryService.class);
    var summaryMapper = beanFactory.getBean(SavingsSummaryMapper.class);
    switch (formatType) {
      case JSON:
      default:
        return new JsonSummaryResponseStrategy(params, summaryService, summaryMapper);
      case EXCEL:
        return new ExcelSummaryResponseStrategy(params, summaryService, summaryMapper);
    }
  }

}
