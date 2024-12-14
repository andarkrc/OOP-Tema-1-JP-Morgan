package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;

public class PrintUsers extends DefaultTransaction{

    public PrintUsers(CommandInput input, Bank bank) {
        super(input, bank);
    }

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
