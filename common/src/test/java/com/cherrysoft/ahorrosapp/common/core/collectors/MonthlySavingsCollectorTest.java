package com.cherrysoft.ahorrosapp.common.core.collectors;

import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.utils.TestUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MonthlySavingsCollectorTest {
  private MonthlySavingsCollector collector;

  @Test
  void givenJuly2022_thenReturns1GroupWith31DailySavings() {
    List<DailySaving> savings = TestUtils.Savings.generateSavingsForMonth("07-2022", 100);
    collector = new MonthlySavingsCollector(savings);

    var result = collector.groupByMonth();

    assertEquals(1, result.size());
    assertSavingsGroup(result.get(0), "07-2022", 31);
  }

  @Test
  void given2ConsecutiveMonths_thenReturns2GroupsAndEachHasItsCorrespondingDailySavingsCount() {
    List<DailySaving> savings = TestUtils.Savings.generateSavingsForMonth("07-2022", 100);
    savings.addAll(TestUtils.Savings.generateSavingsForMonth("08-2022", 100));
    collector = new MonthlySavingsCollector(savings);

    var result = collector.groupByMonth();

    assertEquals(2, result.size());
    assertSavingsGroup(result.get(0), "07-2022", 31);
    assertSavingsGroup(result.get(1), "08-2022", 31);
  }

  @Test
  void given3ConsecutiveMonths_andUnordered_thenReturns3GroupsOrderedAscByMonth() {
    List<DailySaving> savings = new ArrayList<>();
    savings.addAll(TestUtils.Savings.generateSavingsForMonth("09-2022", 100));
    savings.addAll(TestUtils.Savings.generateSavingsForMonth("08-2022", 100));
    savings.addAll(TestUtils.Savings.generateSavingsForMonth("07-2022", 100));
    collector = new MonthlySavingsCollector(savings);

    var result = collector.groupByMonth();

    // July
    assertSavingsGroup(result.get(0), "07-2022", 31);

    // August
    assertSavingsGroup(result.get(1), "08-2022", 31);

    // September
    assertSavingsGroup(result.get(2), "09-2022", 30);
  }

  private void assertSavingsGroup(SavingsGroup group, String expectedName, int expectedSavingsCount) {
    assertThat(group.getGroupName(), is(expectedName));
    assertThat(group.getSavings(), hasSize(expectedSavingsCount));
  }

}
