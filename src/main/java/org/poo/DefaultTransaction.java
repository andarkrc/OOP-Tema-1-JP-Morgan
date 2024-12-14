package org.poo;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;

@Getter
@Setter
public abstract class DefaultTransaction implements Visitable {
    protected String commandName;
    protected int timestamp;
    protected Bank bank;
    protected JsonObject result;
    protected JsonObject details;

    protected DefaultTransaction() {

    }

    public DefaultTransaction(CommandInput input, Bank bank) {
        commandName = input.getCommand();
        timestamp = input.getTimestamp();
        this.bank = bank;
    }

    public String accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public void execute() {

    }

    public void remember() {

    }

    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        result.add("description", "ok");
        return "ok";
    }

    public boolean hasLoggableError() {
        return false;
    }

    public void burnDetails() {
        details = new JsonObject();
        details.add("command", commandName);
        details.add("timestamp", timestamp);
    }
}
