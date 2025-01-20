package org.poo.transactions.payments;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

public final class CashWithdrawal extends DefaultTransaction {
    private String email;
    private String cardNumber;
    private double amount;
    private String location;

    public CashWithdrawal(final CommandInput input, final Bank bank) {
        super(input, bank);
        email = input.getEmail();
        cardNumber = input.getCardNumber();
        amount = input.getAmount();
        location = input.getLocation();
    }

    @Override
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

    @Override
    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }
        bank.addTransaction(email, this);
    }

    @Override
    public boolean hasLoggableError() {
        if (verify().equals("Card not found")) {
            return true;
        }
        if (verify().equals("User not found")) {
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

        Account acc = bank.getAccountWithCard(cardNumber);
        double actualAmount = amount * bank.getExchangeRate("RON", acc.getCurrency());
        double totalAmount = bank.getTotalPrice(actualAmount, acc.getCurrency(), acc.getIban());

        if (acc.getBalance() < totalAmount) {
            details.add("description", "Insufficient funds");
            return;
        }

        details.add("amount", amount);
        details.add("description", "Cash withdrawal of " + amount);
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }
        Account acc = bank.getAccountWithCard(cardNumber);
        double actualAmount = amount * bank.getExchangeRate("RON", acc.getCurrency());
        double totalAmount = bank.getTotalPrice(actualAmount, acc.getCurrency(), acc.getIban());
        Card card = bank.getCard(cardNumber);
        if (card.getStatus().equals("frozen")) {
            //card is frozen
            return;
        }

        if (acc.getBalance() < totalAmount) {
            //insuficient funds
            return;
        }

        if (acc.getMinBalance() > totalAmount) {
            //min balance
            return;
        }

        acc.setBalance(acc.getBalance() - totalAmount);
    }
}
