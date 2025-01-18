package org.poo.transactions.payments;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

import java.time.LocalDate;
import java.time.Period;

public final class WithdrawSavings extends DefaultTransaction {
    private String account;
    private double amount;
    private String currency;

    public WithdrawSavings(CommandInput commandInput, Bank bank) {
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
        if (Period.between(userBirthDay, LocalDate.now()).getYears() < 21) {
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
        details.add("description", "Withdrew " + amount + " to account " + receiver.getIban());
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
        if (Period.between(userBirthDay, LocalDate.now()).getYears() < 21) {
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
}
