package org.poo.bank.accounts;

public final class BusinessAccount extends Account {
    public BusinessAccount() {
        super();
    }

    @Override
    public boolean isBusiness() {
        return true;
    }
}
