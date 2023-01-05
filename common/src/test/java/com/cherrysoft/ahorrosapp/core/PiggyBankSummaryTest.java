package com.cherrysoft.ahorrosapp.core;

import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PiggyBankSummaryTest {
  private PiggyBankSummary piggyBankSummary;
  private PiggyBank piggyBank;

  @BeforeEach
  void setUp() {
    piggyBank = TestUtils.PiggyBanks.newPiggyBankNoEndDate();
    piggyBank.setDailySavings(TestUtils.Savings.generateSavingsForMonth("07-2022", 100));
    piggyBankSummary = new PiggyBankSummary(piggyBank);
  }

  @Test
  void calculatesSavingsTotalAmount() {
    assertEquals(BigDecimal.valueOf(3100.0), piggyBankSummary.getTotalAmount());
  }

  @Test
  void calculatesPiggyBankTotalAmount() {
    piggyBank.setInitialAmount(BigDecimal.valueOf(50));

    assertEquals(BigDecimal.valueOf(3150.0), piggyBankSummary.getTotalAmount());
  }

  @Test
  void calculatesInHandAmount() {
    piggyBank.setBorrowedAmount(BigDecimal.valueOf(200));

    assertEquals(BigDecimal.valueOf(2900.0), piggyBankSummary.getInHandAmount());
  }

  @Test
  void whenHasInitialAmount_andMoneyBorrowed_thenCalculatesInHandAmount() {
    piggyBank.setInitialAmount(BigDecimal.valueOf(50));
    piggyBank.setBorrowedAmount(BigDecimal.valueOf(200));

    assertEquals(BigDecimal.valueOf(2950.0), piggyBankSummary.getInHandAmount());
  }

}
