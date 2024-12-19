package org.poo.transactions.cards;

import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

public final class DeleteCard extends DefaultTransaction {
    private String email;
    private String number;
    private String iban;

    public DeleteCard(final CommandInput input, final Bank bank) {
        super(input, bank);
        email = input.getEmail();
        number = input.getCardNumber();
    }

    public DeleteCard(final DefaultTransaction transaction, final String number) {
        commandName = transaction.getCommandName();
        timestamp = transaction.getTimestamp();
        bank = transaction.getBank();
        this.number = number;
        email = bank.getEntryWithCard(number).getUser().getEmail();
        iban = bank.getAccountWithCard(number).getIban();
    }

    @Override
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

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }


        bank.removeCard(number);
    }

    @Override
    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }
        iban = bank.getAccountWithCard(number).getIban();
        bank.addTransaction(email, this);
    }

    @Override
    public void burnDetails() {
        if (!verify().equals("ok")) {
            return;
        }

        details = new JsonObject();
        details.add("cardHolder", email);
        details.add("card", number);
        details.add("account", bank.getAccountWithCard(number).getIban());
        details.add("timestamp", timestamp);
        details.add("description", "The card has been destroyed");
    }

    @Override
    public String getAccount() {
        return iban;
    }
}
