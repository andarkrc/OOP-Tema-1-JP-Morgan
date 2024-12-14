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

    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(email)) {
            result.add("description", "User does not exist");
            return "User does not exist";
        }
        if (!bank.databaseHas(number)) {
            result.add("description", "Card does not exist");
            return "Card does not exist";
        }
        if (bank.getEntryWithEmail(email) != bank.getEntryWithCard(number)) {
            result.add("description", "User does not own card");
            return "User does not own card";
        }
        result.add("description", "ok");
        return "ok";
    }

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        bank.removeCard(number);
    }

    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }

        bank.addTransaction(email,this);
    }

    public void burnDetails() {
        if (!verify().equals("ok")) {
            return;
        }

        details = new JsonObject();
        details.add("cardHolder", email);
        details.add("card", number);
        details.add("account", bank.getAccountWithCard(number).getIBAN());
        details.add("timestamp", timestamp);
        details.add("description", "The card has been destroyed");
    }
}
