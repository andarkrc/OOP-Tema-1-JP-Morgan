package org.poo.transactions.payments;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;
import org.poo.transactions.accounts.UpgradePlan;
import org.poo.transactions.cards.CreateOneTimeCard;
import org.poo.transactions.cards.DeleteCard;
import org.poo.utils.Constants;

public final class PayOnline extends DefaultTransaction {
    private String cardNumber;
    private double amount;
    private String currency;
    private String description;
    private String commerciant;
    private String email;
    private String iban;

    public PayOnline(final CommandInput input, final Bank bank) {
        super(input, bank);
        cardNumber = input.getCardNumber();
        amount = input.getAmount();
        currency = input.getCurrency();
        description = input.getDescription();
        commerciant = input.getCommerciant();
        email = input.getEmail();
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
        Account account = bank.getAccountWithCard(cardNumber);
        if (account.getPermission(email) < Constants.BASIC_LEVEL) {
            result.add("description", "Card not found");
            return "Card not found";
        }
        if (Double.compare(amount, 0.0) == 0) {
            result.add("description", "Amount is zero");
            return "Amount is zero";
        }

        result.add("description", "ok");
        return "ok";
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        Card card = bank.getCard(cardNumber);
        if (card.getStatus().equals("frozen")) {
            return;
        }

        Account account = bank.getAccountWithCard(cardNumber);

        if (account.getPermission(email) < Constants.BASIC_LEVEL) {
            // no permission for user
            return;
        }

        double actualAmount = amount * bank.getExchangeRate(currency, account.getCurrency());

        if (account.getPermission(email) == Constants.BASIC_LEVEL) {
            if (actualAmount > account.getSpendingLimit()) {
                // spending limit reached
                return;
            }
        }

        String ownerEmail = bank.getEntryWithCard(cardNumber).getUser().getEmail();

        double totalAmount = bank.getTotalPrice(actualAmount, account.getCurrency(), account.getIban());
        if (Double.compare(account.getBalance(), totalAmount) >= 0) {
            account.setBalance(account.getBalance() - totalAmount);
            account.addFunds(bank.getCashBack(amount, currency, account.getIban(), commerciant));
            bank.receiveCoupon(account.getIban(), commerciant);
            boolean shouldUpgrade = bank.updateUserPlan(ownerEmail, amount, currency);
            if (shouldUpgrade) {
                // Create and execute a new UpgradePlan transaction preset for gold
                DefaultTransaction upgrade = new UpgradePlan(timestamp, bank, account.getIban());
                upgrade.burnDetails();
                upgrade.remember();
                upgrade.execute();
            }
            if (card.isOneTime()) {
                DefaultTransaction deleteCard = new DeleteCard(this, cardNumber);
                deleteCard.burnDetails();
                deleteCard.remember();
                deleteCard.execute();
                DefaultTransaction createCard = new CreateOneTimeCard(this, account.getIban());
                createCard.burnDetails();
                createCard.remember();
                createCard.execute();
            }
        }
    }

    @Override
    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }
        iban = bank.getAccountWithCard(cardNumber).getIban();
        String ownerEmail = bank.getEntryWithCard(cardNumber).getUser().getEmail();
        bank.addTransaction(ownerEmail, this);
    }

    @Override
    public void burnDetails() {
        if (!verify().equals("ok")) {
            return;
        }
        details = new JsonObject();
        details.add("timestamp", timestamp);
        Card card = bank.getCard(cardNumber);
        if (card.getStatus().equals("frozen")) {
            details.add("description", "The card is frozen");
            return;
        }

        Account account = bank.getAccountWithCard(cardNumber);

        if (account.getPermission(email) < Constants.BASIC_LEVEL) {
            details.add("description", "User does not have permission");
            return;
        }

        double actualAmount = amount * bank.getExchangeRate(currency, account.getCurrency());

        if (account.getPermission(email) == Constants.BASIC_LEVEL) {
            if (actualAmount > account.getSpendingLimit()) {
                details.add("description", "Spending limit reached");
                return;
            }
        }

        double totalAmount = bank.getTotalPrice(actualAmount, account.getCurrency(), account.getIban());
        if (account.getBalance() < totalAmount) {
            details.add("description", "Insufficient funds");
        } else {
            details.add("description", "Card payment");
            details.add("amount", actualAmount);
            details.add("commerciant", commerciant);
        }
    }

    @Override
    public String getAccount() {
        return iban;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public boolean appearsInSpendingsReport() {
        return details.getStringOfField("description").equals("Card payment");
    }

    @Override
    public boolean isPayment() {
        return true;
    }
}
