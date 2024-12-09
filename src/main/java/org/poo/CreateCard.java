package org.poo;

import org.poo.fileio.CommandInput;

public abstract class CreateCard extends DefaultTransaction{
    protected String IBAN;
    protected String email;

    public CreateCard(CommandInput input, Bank bank) {
        super(input, bank);
        IBAN = input.getAccount();
        email = input.getEmail();
    }
}
