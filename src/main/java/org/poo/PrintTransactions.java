package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;

public class PrintTransactions extends DefaultTransaction {
    private String email;

    public PrintTransactions(CommandInput input, Bank bank) {
        super(input, bank);
        email = input.getEmail();
    }

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

    public void execute() {
        JsonObject command = new JsonObject();
        command.add("command", commandName);
        command.add("timestamp", timestamp);
        JsonArray transactions = new JsonArray();
        for (DefaultTransaction transaction : bank.getEntryWithEmail(email).getTransactionHistory()) {
            transactions.add(transaction.getDetails());
        }
        command.add("output", transactions);
        bank.getOutput().add(command);
    }
}
