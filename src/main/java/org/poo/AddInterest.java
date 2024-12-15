package org.poo;

import org.poo.fileio.CommandInput;

public class AddInterest extends DefaultTransaction {
    private String account;

    public AddInterest(CommandInput input, Bank bank) {
        super(input, bank);
        account = input.getAccount();
    }

    public String getAccount() {
        return account;
    }

}
