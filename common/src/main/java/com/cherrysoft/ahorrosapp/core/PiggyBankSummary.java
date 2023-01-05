package com.cherrysoft.ahorrosapp.core;

import com.cherrysoft.ahorrosapp.core.models.PiggyBank;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNullElse;

public class PiggyBankSummary extends IntervalSavingsSummary {
  private final PiggyBank pb;

  public PiggyBankSummary(PiggyBank pb) {
    super(pb.getDailySavings());
    this.pb = pb;
  }

  @Override
  public BigDecimal getTotalAmount() {
    BigDecimal savingsTotalAmount = super.getTotalAmount();
    return savingsTotalAmount.add(getInitialAmount());
  }

  public BigDecimal getInHandAmount() {
    return getTotalAmount().subtract(getBorrowedAmount());
  }

  public BigDecimal getBorrowedAmount() {
    BigDecimal borrowedAmount = pb.getBorrowedAmount();
    return requireNonNullElse(borrowedAmount, BigDecimal.ZERO);
  }

  public BigDecimal getInitialAmount() {
    BigDecimal initialAmount = pb.getInitialAmount();
    return requireNonNullElse(initialAmount, BigDecimal.ZERO);
  }

  public PiggyBank getPiggyBank() {
    return pb;
  }

}
