package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;

public class PrintUsers extends DefaultTransaction{

    public PrintUsers(CommandInput input, Bank bank) {
        super(input, bank);
    }

    public void execute() {
        BankVisitor visitor = new BankVisitor();
        JsonObject output = new JsonObject();
        output.add("command", commandName);
        output.add("timestamp", timestamp);
        output.add("output", visitor.visit(bank));
        bank.getOutput().add(output);
    }

    public void remember() {

    }
}
