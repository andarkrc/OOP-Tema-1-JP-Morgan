package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;

public class ChangeInterestRate extends DefaultTransaction {
    private double interestRate;
    private String account;

    public ChangeInterestRate(CommandInput input, Bank bank) {
        super(input, bank);
        interestRate = input.getInterestRate();;
        account = input.getAccount();
    }

    public void burnDetails() {
        details = new JsonObject();
        details.add("timestamp", timestamp);
        details.add("description", "Interest rate of the account changed to " + interestRate);
    }

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

    public boolean hasLoggableError() {
        if (verify().equals("Account is not savings")) {
            return true;
        }
        return false;
    }

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        bank.getAccountWithIBAN(account).setInterestRate(interestRate);
    }

    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }

        bank.addTransaction(bank.getEntryWithIBAN(account).getUser().getEmail(), this);
    }
}
