package org.poo.transactions.payments;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;
import org.poo.utils.Constants;

import java.time.LocalDate;
import java.time.Period;

public final class WithdrawSavings extends DefaultTransaction {
    private String account;
    private double amount;
    private String currency;

    public WithdrawSavings(final CommandInput commandInput, final Bank bank) {
        super(commandInput, bank);
        account = commandInput.getAccount();
        amount = commandInput.getAmount();
        currency = commandInput.getCurrency();
    }

    @Override
    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(account)) {
            result.add("description", "Account not found");
            return "Account not found";
        }

        result.add("description", "ok");
        return "ok";
    }

    @Override
    public void burnDetails() {
        if (!verify().equals("ok")) {
            return;
        }
        details = new JsonObject();
        details.add("timestamp", timestamp);

        DatabaseEntry user = bank.getEntryWithIBAN(account);
        Account savings = bank.getAccountWithIBAN(account);

        LocalDate userBirthDay = user.getUser().getBirthDate();
        if (Period.between(userBirthDay, LocalDate.now()).getYears()
                < Constants.MIN_WITHDRAW_AGE) {
            details.add("description", "You don't have the minimum age required.");
            return;
        }

        if (!savings.isSavings()) {
            details.add("description", "Account is not of type savings.");
            return;
        }
        Account receiver = null;
        for (Account acc : user.getAccounts()) {
            if (acc.getCurrency().equals(currency) && acc.isClassic()) {
                receiver = acc;
                break;
            }
        }
        if (receiver == null) {
            details.add("description", "You do not have a classic account.");
            return;
        }
        details.add("amount", amount);
        details.add("classicAccountIBAN", receiver.getIban());
        details.add("savingsAccountIBAN", savings.getIban());
        details.add("description", "Savings withdrawal");

        String email = bank.getEntryWithIBAN(savings.getIban()).getUser().getEmail();
        bank.addTransaction(email, this); // remember twice for classic account
    }

    @Override
    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }
        String email = bank.getEntryWithIBAN(account).getUser().getEmail();
        bank.addTransaction(email, this);
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        DatabaseEntry user = bank.getEntryWithIBAN(account);
        Account savings = bank.getAccountWithIBAN(account);

        LocalDate userBirthDay = user.getUser().getBirthDate();
        if (Period.between(userBirthDay, LocalDate.now()).getYears()
                < Constants.MIN_WITHDRAW_AGE) {
            //not old enough
            return;
        }

        if (!savings.isSavings()) {
            //acount not savings
            return;
        }

        Account receiver = null;
        for (Account acc : user.getAccounts()) {
            if (acc.getCurrency().equals(currency) && acc.isClassic()) {
                receiver = acc;
                break;
            }
        }

        if (receiver == null) {
            // no classic account owned
            return;
        }

        receiver.addFunds(amount);
        savings.setBalance(savings.getBalance() - amount);
    }

    @Override
    public String getAccount() {
        return account;
    }
}
