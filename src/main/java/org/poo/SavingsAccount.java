package org.poo;

import org.poo.jsonobject.JsonObject;

public class SavingsAccount extends Account {
    private double interestRate;
    SavingsAccount() {
        super();
    }

    public Account setInterestRate(double interestRate) {
        this.interestRate = interestRate;
        return this;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public boolean isSavings() {
        return true;
    }
}
