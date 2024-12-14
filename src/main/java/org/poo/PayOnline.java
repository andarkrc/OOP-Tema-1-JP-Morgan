package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;

public class PayOnline extends DefaultTransaction {

    private String cardNumber;
    private double amount;
    private String currency;
    private String description;
    private String commerciant;
    private String email;

    public PayOnline(CommandInput input, Bank bank) {
        super(input, bank);
        cardNumber = input.getCardNumber();
        amount = input.getAmount();
        currency = input.getCurrency();
        description = input.getDescription();
        commerciant = input.getCommerciant();
        email = input.getEmail();
    }

    public boolean hasLoggableError() {
        String error = verify();
        if (error.equals("Card not found")) {
            return true;
        }
        return false;
    }

    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(email)) {
            result.add("description", "User not found");
            return "User not found";
        }
        if (!bank.databaseHas(cardNumber)) {
            result.add("description", "Card not found");
            return "Card not found";
        }

        result.add("description", "ok");
        return "ok";
    }

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }


        Account account = bank.getAccountWithCard(cardNumber);
        double actualAmount = amount * bank.getExchangeRate(currency, account.getCurrency());
        if (account.getBalance() >= actualAmount) {
            account.setBalance(account.getBalance() - actualAmount);
        }
    }

    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }

        bank.addTransaction(email, this);
    }

    public void burnDetails() {
        if (!verify().equals("ok")) {
            return;
        }
        details = new JsonObject();
        details.add("timestamp", timestamp);

        Account account = bank.getAccountWithCard(cardNumber);
        double actualAmount = amount * bank.getExchangeRate(currency, account.getCurrency());
        if (account.getBalance() < actualAmount) {
            details.add("description", "Insufficient funds");
        } else {
            details.add("description", "Card payment");
            details.add("amount", actualAmount);
            details.add("commerciant", commerciant);
        }
    }
}
