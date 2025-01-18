package org.poo.transactions.cards;

import org.poo.transactions.DefaultTransaction;
import org.poo.utils.Constants;
import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;

public final class CheckCardStatus extends DefaultTransaction {
    private String number;

    public CheckCardStatus(final CommandInput input, final Bank bank) {
        super(input, bank);
        number = input.getCardNumber();
    }

    @Override
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

    @Override
    public boolean hasLoggableError() {
        String error = verify();
        if (error.equals("Card not found")) {
            return true;
        }

        return false;
    }

    @Override
    public void burnDetails() {
        if (!verify().equals("ok")) {
            return;
        }

        details = new JsonObject();
        details.add("timestamp", timestamp);
        Account account = bank.getAccountWithCard(number);
        if (account.getBalance() <= account.getMinBalance()) {
            details.add("description",
                    "You have reached the minimum amount of funds, the card will be frozen");
        } else if (account.getBalance() - account.getMinBalance() <= Constants.WARNING_AMOUNT) {
            details.add("description", "Warning");
        } else {
            details.add("description", "All good");
        }
    }

    @Override
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

    @Override
    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }
        Account acc = bank.getAccountWithCard(number);
        if (acc.getBalance() > acc.getMinBalance() + Constants.WARNING_AMOUNT) {
            return;
        }
        bank.addTransaction(bank.getEntryWithCard(number).getUser().getEmail(), this);
    }
}
