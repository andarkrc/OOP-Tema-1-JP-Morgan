package org.poo.transactions.reports;

import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

public final class PrintTransactions extends DefaultTransaction {
    private String email;

    public PrintTransactions(final CommandInput input, final Bank bank) {
        super(input, bank);
        email = input.getEmail();
    }

    @Override
    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(email)) {
            result.add("description", "No user found");
            return "User not found";
        }
        result.add("description", "ok");
        return "ok";
    }

    @Override
    public void execute() {
        JsonObject command = new JsonObject();
        command.add("command", commandName);
        command.add("timestamp", timestamp);
        JsonArray transactions = new JsonArray();
        for (DefaultTransaction transaction : bank.getEntryWithEmail(email).
                                                   getTransactionHistory()) {
            transactions.add(transaction.getDetails());
        }
        command.add("output", transactions);
        bank.getOutput().add(command);
    }
}
