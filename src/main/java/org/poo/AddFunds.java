package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;

public class AddFunds extends DefaultTransaction {
    private String iban;
    private double amount;

    public AddFunds(CommandInput input, Bank bank) {
        super(input, bank);
        iban = input.getAccount();
        amount = input.getAmount();
    }

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

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        bank.getAccountWithIBAN(iban).addFunds(amount);
    }

    public String getAccount() {
        return iban;
    }
}
