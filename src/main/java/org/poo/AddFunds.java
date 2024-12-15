package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;

import java.math.BigDecimal;

public class AddFunds extends DefaultTransaction {
    private String IBAN;
    private double amount;

    public AddFunds(CommandInput input, Bank bank) {
        super(input, bank);
        IBAN = input.getAccount();
        amount = input.getAmount();
    }

    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(IBAN)) {
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

        bank.getAccountWithIBAN(IBAN).addFunds(amount);
    }

    public String getAccount() {
        return IBAN;
    }
}
