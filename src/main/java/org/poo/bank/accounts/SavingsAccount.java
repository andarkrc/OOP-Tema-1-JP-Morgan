package org.poo.bank.accounts;

public final class SavingsAccount extends Account {
    private double interestRate;
    public SavingsAccount() {
        super();
    }

    @Override
    public Account setInterestRate(final double newInterestRate) {
        this.interestRate = newInterestRate;
        return this;
    }

    @Override
    public double getInterestRate() {
        return interestRate;
    }

    @Override
    public boolean isSavings() {
        return true;
    }
}
