package org.poo.transactions.accounts;

import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

public final class AddFunds extends DefaultTransaction {
    private String iban;
    private double amount;

    public AddFunds(final CommandInput input, final Bank bank) {
        super(input, bank);
        iban = input.getAccount();
        amount = input.getAmount();
    }

    @Override
    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(iban)) {
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

        bank.getAccountWithIBAN(iban).addFunds(amount);
    }

    @Override
    public String getAccount() {
        return iban;
    }
}
