package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;

public abstract class CreateCard extends DefaultTransaction{
    protected String IBAN;
    protected String email;

    public CreateCard(CommandInput input, Bank bank) {
        super(input, bank);
        IBAN = input.getAccount();
        email = input.getEmail();
    }

    protected boolean verify() {
        if (!bank.databaseHas(email)) {
            System.out.println("User does not exist");
            return false;
        }

        if (!bank.databaseHas(IBAN)) {
            System.out.println("Account does not exist");
            return false;
        }

        if (bank.getEntryWithEmail(email) != bank.getEntryWithIBAN(IBAN))  {
            System.out.println("User does not own account");
            return false;
        }

        return true;
    }
}
