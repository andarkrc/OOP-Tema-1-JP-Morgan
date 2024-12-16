package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;

public class CheckCardStatus extends DefaultTransaction {
    private String number;

    public CheckCardStatus(CommandInput input, Bank bank) {
        super(input, bank);
        number = input.getCardNumber();
    }

    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(number)) {
            result.add("description", "Card not found");
            return "Card not found";
        }
        if (bank.getCard(number).getStatus().equals("frozen")) {
            result.add("description", "Card is frozen");
            return "Card is frozen";
        }
        result.add("description", "ok");
        return "ok";
    }

    public boolean hasLoggableError() {
        String error = verify();
        if (error.equals("Card not found")) {
            return true;
        }

        return false;
    }

    public void burnDetails() {
        if (!verify().equals("ok")) {
            return;
        }

        details = new JsonObject();
        details.add("timestamp", timestamp);
        Account account = bank.getAccountWithCard(number);
        if (account.getBalance() <= account.getMinBalance()) {
            details.add("description", "You have reached the minimum amount of funds, the card will be frozen");
        } else if (account.getBalance() - account.getMinBalance() <= 30) {
            details.add("description", "Warning");
        } else {
            details.add("description", "All good");
        }
    }

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }
        Card card = bank.getCard(number);
        Account account = bank.getAccountWithCard(number);
        if (account.getBalance() <= account.getMinBalance()) {
            card.setStatus("frozen");
        }
    }

    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }
        Account acc = bank.getAccountWithCard(number);
        if (acc.getBalance() > acc.getMinBalance() + 30) {
            return;
        }
        bank.addTransaction(bank.getEntryWithCard(number).getUser().getEmail(), this);
    }
}
