package org.poo;


import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;

public abstract class AddAccount extends DefaultTransaction {
    protected String email;
    protected String currency;
    protected String accountType;

    public AddAccount(CommandInput input, Bank bank) {
        super(input, bank);
        email = input.getEmail();
        currency = input.getCurrency();
        accountType = input.getAccountType();
    }
}
