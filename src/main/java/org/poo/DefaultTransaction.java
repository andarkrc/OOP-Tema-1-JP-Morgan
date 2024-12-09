package org.poo;

import lombok.Setter;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;

@Setter
public abstract class DefaultTransaction implements Transaction {
    protected String commandName;
    protected int timestamp;
    protected Bank bank;

    public DefaultTransaction(CommandInput input, Bank bank) {
        commandName = input.getCommand();
        timestamp = input.getTimestamp();
        this.bank = bank;
    }
}
