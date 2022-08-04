package com.cherrysoft.ahorrosapp.core;

import com.cherrysoft.ahorrosapp.core.models.PiggyBank;

import java.math.BigDecimal;

import static java.util.Objects.isNull;

public class PiggyBankSummary extends IntervalSavingsSummary {
  private final PiggyBank pb;

  public PiggyBankSummary(PiggyBank pb) {
    super(pb.getDailySavings());
    this.pb = pb;
  }

  public BigDecimal getPiggyBankTotalAmount() {
    BigDecimal savingsTotalAmount = getSavingsTotalAmount();
    return savingsTotalAmount.add(getInitialAmount());
  }

  public BigDecimal getInHandAmount() {
    return getPiggyBankTotalAmount().subtract(getBorrowedAmount());
  }

  public BigDecimal getBorrowedAmount() {
    BigDecimal borrowedAmount = pb.getBorrowedAmount();
    if (isNull(borrowedAmount)) {
      return BigDecimal.ZERO;
    }
    return borrowedAmount;
  }

  public BigDecimal getInitialAmount() {
    BigDecimal initialAmount = pb.getInitialAmount();
    if (isNull(initialAmount)) {
      return BigDecimal.ZERO;
    }
    return initialAmount;
  }

}