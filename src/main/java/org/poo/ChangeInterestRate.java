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

    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(account)) {
            result.add("description", "Account not found");
            return "Account not found";
        }
        if (!bank.getAccountWithIBAN(account).isSavings()) {
            result.add("description", "Account not savings");
            return "Account not savings";
        }
        result.add("description", "ok");
        return "ok";
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
