package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;

public class SetMinimumBalance extends DefaultTransaction {
    private double amount;
    private String account;

    SetMinimumBalance(CommandInput input, Bank bank) {
        super(input, bank);
        amount = input.getAmount();
        account = input.getAccount();
    }

    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(account)) {
            result.add("description", "Account not found");
            return "Account not found";
        }
        result.add("description", "ok");
        return "ok";
    }

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        bank.getAccountWithIBAN(account).setMinBalance(amount);
    }

    public void burnDetails() {
        if (!verify().equals("ok")) {
            return;
        }

        details = new JsonObject();
        details.add("timestamp", timestamp);
        details.add("command", commandName);
    }
}
