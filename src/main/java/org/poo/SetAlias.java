package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;

public class SetAlias extends DefaultTransaction {
    private String email;
    private String account;
    private String alias;

    public SetAlias(CommandInput input, Bank bank) {
        super(input, bank);
        email = input.getEmail();
        account = input.getAccount();
        alias = input.getAlias();
    }

    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(email)) {
            result.add("description", "No user found");
            return "No user found";
        }
        return "ok";
    }

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        DatabaseEntry entry = bank.getEntryWithEmail(email);
        entry.setAlias(account, alias);
    }
}
