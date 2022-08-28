package com.cherrysoft.ahorrosapp.core.splitters;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.utils.TestUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MonthlySavingsSplitterTest {
  private MonthlySavingsSplitter splitter;

  @Test
  void givenJuly2022_thenReturns1SplitWith31DailySavings() {
    List<DailySaving> savings = TestUtils.Savings.generateSavingsForMonth("07-2022", 100);
    splitter = new MonthlySavingsSplitter(savings);

    var result = splitter.split();

    assertEquals(1, result.size());
    assertEquals("07-2022", result.get(0).splitRepresentation());
    assertThat(result.get(0).getDailySavings(), hasSize(31));
  }

  @Test
  void given3ConsecutiveMonths_thenReturns3SplitsAndEachSplitHasItsCorrespondingDailySavingsCount() {
    List<DailySaving> savings = TestUtils.Savings.generateSavingsForMonth("07-2022", 100);
    savings.addAll(TestUtils.Savings.generateSavingsForMonth("08-2022", 100));
    savings.addAll(TestUtils.Savings.generateSavingsForMonth("09-2022", 100));
    splitter = new MonthlySavingsSplitter(savings);

    var result = splitter.split();

    assertEquals(3, result.size());
    assertThat(result.get(0).getDailySavings(), hasSize(31));
    assertThat(result.get(1).getDailySavings(), hasSize(31));
    assertThat(result.get(2).getDailySavings(), hasSize(30));
  }

}