package org.poo;


import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.utils.Utils;

public abstract class AddAccount extends DefaultTransaction {
    protected String email;
    protected String currency;
    protected String accountType;
    protected String iban;

    public AddAccount(CommandInput input, Bank bank) {
        super(input, bank);
        email = input.getEmail();
        currency = input.getCurrency();
        accountType = input.getAccountType();
        iban = Utils.generateIBAN();
    }

    public void burnDetails() {
        details = new JsonObject();
        details.add("timestamp", timestamp);
        details.add("description", "New account created");
    }

    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(email)) {
            result.add("description", "No such user");
            return "No such user";
        }
        result.add("description", "ok");
        return "ok";
    }

    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }
        bank.addTransaction(email, this);
    }

    public String getAccount() {
        return iban;
    }
}
