package org.poo.transactions.reports;

import org.poo.transactions.DefaultTransaction;
import org.poo.visitors.JsonObjectVisitor;
import org.poo.bank.Bank;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;

public final class PrintUsers extends DefaultTransaction {

    public PrintUsers(final CommandInput input, final Bank bank) {
        super(input, bank);
    }

    @Override
    public void execute() {
        JsonObjectVisitor visitor = new JsonObjectVisitor();
        JsonObject output = new JsonObject();
        output.add("command", commandName);
        output.add("timestamp", timestamp);
        JsonArray dbEntries = new JsonArray();
        for (DatabaseEntry dbEntry : bank.getDatabase().getEntries()) {
            dbEntries.addStringNoQuotes(dbEntry.accept(visitor));
        }
        output.add("output", dbEntries);
        bank.getOutput().add(output);
    }
}
