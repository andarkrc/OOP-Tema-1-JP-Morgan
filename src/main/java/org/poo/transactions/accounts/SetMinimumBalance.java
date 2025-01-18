package org.poo.transactions.accounts;

import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

public final class SetMinimumBalance extends DefaultTransaction {
    private double amount;
    private String account;

    public SetMinimumBalance(final CommandInput input, final Bank bank) {
        super(input, bank);
        amount = input.getAmount();
        account = input.getAccount();
    }

    @Override
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

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        bank.getAccountWithIBAN(account).setMinBalance(amount);
    }
}
