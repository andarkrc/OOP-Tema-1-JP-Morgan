package org.poo.transactions.accounts;

import org.poo.bank.Bank;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

public final class SetAlias extends DefaultTransaction {
    private String email;
    private String account;
    private String alias;

    public SetAlias(final CommandInput input, final Bank bank) {
        super(input, bank);
        email = input.getEmail();
        account = input.getAccount();
        alias = input.getAlias();
    }

    @Override
    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(email)) {
            result.add("description", "No user found");
            return "No user found";
        }
        return "ok";
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        DatabaseEntry entry = bank.getEntryWithEmail(email);
        entry.setAlias(account, alias);
    }
}
