package org.poo.transactions.accounts;

import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

public final class ChangeInterestRate extends DefaultTransaction {
    private double interestRate;
    private String account;

    public ChangeInterestRate(final CommandInput input, final Bank bank) {
        super(input, bank);
        interestRate = input.getInterestRate();
        account = input.getAccount();
    }

    @Override
    public void burnDetails() {
        if (!verify().equals("ok")) {
            return;
        }

        details = new JsonObject();
        details.add("timestamp", timestamp);
        details.add("description", "Interest rate of the account changed to " + interestRate);
    }

    @Override
    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(account)) {
            result.add("description", "Account not found");
            return "Account not found";
        }
        if (!bank.getAccountWithIBAN(account).isSavings()) {
            result.add("description", "This is not a savings account");
            return "Account is not savings";
        }
        result.add("description", "ok");
        return "ok";
    }

    @Override
    public boolean hasLoggableError() {
        if (verify().equals("Account is not savings")) {
            return true;
        }
        return false;
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        bank.getAccountWithIBAN(account).setInterestRate(interestRate);
    }

    @Override
    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }

        bank.addTransaction(bank.getEntryWithIBAN(account).getUser().getEmail(), this);
    }

    @Override
    public String getAccount() {
        return account;
    }
}
