package com.cherrysoft.ahorrosapp.controllers.summaries.factories;

import com.cherrysoft.ahorrosapp.controllers.summaries.reponsestrategies.SummaryResponseStrategy;
import com.cherrysoft.ahorrosapp.core.queryparams.SavingsSummaryQueryParams;

public interface SummaryResponseFormatFactory {

  SummaryResponseStrategy createSummaryResponseStrategy(SavingsSummaryQueryParams params);

}