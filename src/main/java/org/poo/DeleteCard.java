package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;

public class DeleteCard extends DefaultTransaction{
    private String email;
    private String number;

    public DeleteCard(CommandInput input, Bank bank) {
        super(input, bank);
        email = input.getEmail();
        number = input.getCardNumber();
    }

    private boolean verify() {
        if (!bank.databaseHas(email)) {
            System.out.println("User does not exist");
            return false;
        }
        if (!bank.databaseHas(number)) {
            System.out.println("Card does not exist");
            return false;
        }
        if (bank.getEntryWithEmail(email) != bank.getEntryWithCard(number)) {
            System.out.println("User does not card");
            return false;
        }
        return true;
    }

    public void execute() {
        if (!verify()) {
            return;
        }

        bank.removeCard(number);
    }

    public void remember() {
        if (!verify()) {
            return;
        }

        bank.addTransaction(email,this);
    }

}
