package com.cherrysoft.ahorrosapp.controllers.summaries.factories;

import com.cherrysoft.ahorrosapp.controllers.summaries.reponsestrategies.SummaryResponseStrategy;
import com.cherrysoft.ahorrosapp.core.params.SavingsSummaryParams;

public interface SummaryResponseFormatFactory {

  SummaryResponseStrategy createSummaryResponseStrategy(SavingsSummaryParams params);

}
